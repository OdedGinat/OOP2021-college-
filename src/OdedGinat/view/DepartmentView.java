package OdedGinat.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import OdedGinat.listeners.ViewListenable;
import OdedGinat.model.Department;
import OdedGinat.model.Message;
import OdedGinat.model.Role;
import OdedGinat.model.Worker;

import java.util.Vector;

public class DepartmentView {
    private Vector<ViewListenable> allListeners;
    private View view;
    private GridPane mainGP;
    private WorkersView workersView;

    public DepartmentView(View view, Vector<ViewListenable> allListeners){
        this.allListeners = allListeners;
        this.view = view;
    }

    public void setWorkersView(WorkersView workersView) {
        this.workersView = workersView;
    }

    public void quickRefresh(){
        refreshDepartmentGrid(mainGP);
    }

    public GridPane refreshDepartmentGrid(GridPane gp){
        mainGP = gp;
        mainGP.getChildren().clear();
        Vector<Department> departments = new Vector<>();
        for (ViewListenable l:
                allListeners) {
            departments = l.getDepartments();
        }
        int row = 1;
        if(departments == null)
            return gp;
        Label lblAllRev = new Label();
        for(ViewListenable l : allListeners) {
             lblAllRev = new Label("Monthly revenue: " + l.getMonthlyRevenue());
        }
        Button addBtn = new Button("+ Department");
        gp.add(addBtn, 0, 0);
        gp.add(lblAllRev, 1, 0);
        for(Department d : departments){
            mainGP.add(departmentGrid(d), 0, row);
            row++;
        }

        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (ViewListenable l:
                        allListeners) {
                    message(l.addDepartment(""));
                    refreshDepartmentGrid(gp);
                }
            }
        });

        return gp;
    }

    public GridPane departmentGrid(Department d){
        GridPane gpd = new GridPane();
        Vector<Role> roles = d.getRoles();
        Label lblName;
        TextField tfName = new TextField();
        tfName.setText(d.getName());

        lblName = new Label("Department name: ");
        gpd.add(tfName, 1, 0);

        Button addRole = new Button("+ Role");
        Button btnRemove = new Button("-");
        Label lblMonthlyRev = new Label();
        CheckBox sync = new CheckBox("Sync roles");
        ComboBox<Integer> syncHour = getHoursCombo();
        CheckBox syncHome = new CheckBox("Working from home");
        sync.setSelected(d.isSyncable());
        if(d.isSyncable()) {
            syncHour.getSelectionModel().select(d.getSyncedTime());
            syncHome.setSelected(d.getSyncedHome());
        }
        lblMonthlyRev.setText("Monthly revenue: " + d.getMonthlyRevenue());
        syncHour.setVisible(d.isSyncable());
        syncHome.setVisible(d.isSyncable());



        gpd.add(lblName, 0, 0);
        gpd.add(sync, 2, 0);
        gpd.add(syncHour, 3, 0);
        gpd.add(syncHome, 4, 0);
        gpd.add(addRole, 5, 0);
        gpd.add(btnRemove, 6, 0);
        gpd.add(lblMonthlyRev, 7, 0);
        int row = 1;
        for(Role r : roles){
            Label roleName = new Label(d.getName()+" -            Role: ");
            TextField tfRole = new TextField();
            tfRole.setText(r.getDescription());
            ComboBox<Worker> workerComboBox = new ComboBox<>();
            tfRole.setMaxWidth(100);
            Button btnRemoveRole = new Button("-");
            ComboBox<Integer> roleHour = getHoursCombo();
            CheckBox roleHome = new CheckBox("Working from home");
            Label lblProductivity = new Label("");
            roleHome.setSelected(r.getWorkFromHome());
            roleHour.getSelectionModel().select(r.getStartTime());
            for(ViewListenable l : allListeners){
                workerComboBox.getItems().addAll(l.getWorkers());
            }

            if(r.getRoleAssignment() != null){
                workerComboBox.setVisible(false);
                Label lblWorker = new Label("Worker: " + r.getRoleAssignment().getName());
                lblProductivity.setText("Monthly revenue: " + r.getMonthlyRevenue());
                gpd.add(lblWorker, 2, row);
//                System.out.println(r.getProductivityModifier()+" "+ r.getRoleAssignment().getPreferredStartingHour() +" "+ r.getStartTime());
            }

            if(d.isSyncable()){
                roleHour.setVisible(false);
                roleHome.setVisible(false);
                Label lblHour = new Label(""+r.getStartTime());
                Label lblHome;
                if(r.getWorkFromHome()){
                    lblHome = new Label("Works from home");
                }
                else {
                    lblHome = new Label("Works from office");
                }
                gpd.add(lblHour, 3, row);
                gpd.add(lblHome, 4, row);
            }
            else {
                roleHour.setVisible(true);
                roleHome.setVisible(true);
            }


            btnRemoveRole.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Boolean b = d.removeRole(r);
                    message(new Message("Removed role " + r.getDescription()));
                    refreshDepartmentGrid(mainGP);
                }
            });

            tfRole.setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    r.setDescription(tfRole.getText());
                    workersView.quickRefresh();
                }
            });

            roleHome.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        r.setWorkConditions(roleHour.getValue(), roleHome.isSelected());
                        quickRefresh();

                    } catch (Exception e) {
                        message("Cannot update work from home for " + r.getDescription(), "#FF0000");
                    }
                }
            });

            roleHour.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        r.setWorkConditions(roleHour.getValue(), roleHome.isSelected());
                        quickRefresh();
                    } catch (Exception e) {
                        message("Cannot update hour for " + r.getDescription(), "#FF0000");
                    }
                }
            });

            workerComboBox.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    for(ViewListenable l : allListeners){
                        l.assignWorker(workerComboBox.getValue(), r);
                        refreshDepartmentGrid(mainGP);
                        workersView.quickRefresh();
                    }
                }
            });
            workerComboBox.setMaxWidth(90);
            gpd.add(roleName, 0, row);
            gpd.add(tfRole, 1, row);
            gpd.add(workerComboBox, 2, row);
            gpd.add(roleHour, 3, row);
            gpd.add(roleHome, 4, row);
            gpd.add(btnRemoveRole, 6, row);
            gpd.add(lblProductivity, 7, row);
            row++;
        }

        addRole.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Role r = new Role("");
                if(d.isSyncable()){
                    r.sync(d.getSyncedTime(), d.getSyncedHome());
                }
                else {
                    try {
                        r.setWorkConditions(8, false);
                    } catch (Exception e) {
                        message(new Message("Error adding role", true));
                    }
                }
                d.addRole(r);

                message("Added new role", "#0000FF");
                refreshDepartmentGrid(mainGP);
            }
        });

        sync.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(d.getName() == "")
                    d.setName(tfName.getText());
                try {
                    if(!d.isSyncable())
                        d.sync(8, false);
                    else
                        d.unSync();
                } catch (Exception e) {
                    message("not a valid time", "#FF0000");
                }
                refreshDepartmentGrid(mainGP);

            }
        });

        syncHour.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                d.setSyncConditions(syncHour.getSelectionModel().getSelectedItem(), syncHome.isSelected());
                refreshDepartmentGrid(mainGP);
            }
        });

        syncHome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                d.setSyncConditions(syncHour.getSelectionModel().getSelectedItem(), syncHome.isSelected());
                refreshDepartmentGrid(mainGP);
            }
        });

        tfName.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                d.setName(tfName.getText());
            }
        });

        btnRemove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for(ViewListenable l: allListeners){
                    message(l.removeDepartment(d));

                }
                refreshDepartmentGrid(mainGP);
            }
        });
        sync.setMinWidth(90);
        gpd.setHgap(10);
        return gpd;
    }

    public ComboBox<Integer> getHoursCombo(){
        ComboBox<Integer> hours = new ComboBox<>();
        Vector<Integer> nums = new Vector<>();
        for (int i = 0; i < 24; i++){
            nums.add(i);
        }
        hours.getItems().addAll(nums);
        return hours;
    }

    public void message(String s, String color){
        view.message(s, color);
    }

    public void message(Message m){
        if(m.getError()){
            message(m.getText(), "#FF0000");
        }
        else {
            message(m.getText(), "#0000FF");
        }
    }
}
