package OdedGinat.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import OdedGinat.listeners.ViewListenable;
import OdedGinat.model.HourlyWorker;
import OdedGinat.model.Message;
import OdedGinat.model.Salary;
import OdedGinat.model.Worker;

import java.util.Vector;

public class WorkersView {
    private Vector<ViewListenable> allListeners;
    private View view;
    private DepartmentView departmentView;
    private GridPane mainGP;


    public WorkersView(View view, Vector<ViewListenable> allListeners){
        this.allListeners = allListeners;
        this.view = view;
    }

    public void setDepartmentView(DepartmentView departmentView) {
        this.departmentView = departmentView;
    }

    public void quickRefresh(){
        refreshWorkerGrid(mainGP);
    }

    public GridPane refreshWorkerGrid(GridPane gp){
        mainGP = gp;
        gp.getChildren().clear();
    //        GridPane workersPane = new GridPane();
    Vector<Worker> workers = new Vector<>();
        for (ViewListenable l:
    allListeners) {
        workers = l.getAllWorkers();
    }
    //        gp.add(sPane, 0, 0);
    int row = 1;
        if(workers == null)
            return gp;
    //        Collections.reverse(workers);
    Button addBtn = new Button("+ Worker");
    addWorker(gp, addBtn);
        for (Worker w:
    workers) {
        GridPane gpw = createWorkerGrid(w, gp);
        gp.add(gpw, 1, row);
        Label num = new Label("ID:"+w.getId());
        gp.add(num, 0, row);
        row++;
    }
        gp.add(addBtn,1, 0);
        return gp;
}

    public void addWorker(GridPane gp, Button btn){
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (ViewListenable l :
                        allListeners) {
                    message(l.addWorker("", 8, false, 0));
                }
                refreshWorkerGrid(gp);
                departmentView.quickRefresh();
            }
        });
    }

    public GridPane createWorkerGrid(Worker w, GridPane gp){
        GridPane gpw = new GridPane();
        gpw.setHgap(10);
        Label lblName = new Label("Name: ");
        TextField tfName = new TextField(w.getName());
        tfName.setMaxWidth(100);
        Label lblPreference = new Label("Preference: ");
        Label lblHour = new Label("Start hour: ");
        ComboBox<Integer> hours = getHoursCombo();
        hours.getSelectionModel().select(w.getPreferredStartingHour());
        CheckBox cboxHome = new CheckBox("Work from home");
        cboxHome.setSelected(w.getPreferWorkingHome());

        Button btnRmv = new Button(" - Worker ");

        if(w.getRole() != null){
            Label lblRole = new Label("Role: " + w.getRole().getDescription());
            Button btnClearRole = new Button("Clear role");

            btnClearRole.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    for(ViewListenable l : allListeners){
                        l.unassignWorker(w);
                    }
                    refreshWorkerGrid(mainGP);
                    departmentView.quickRefresh();
                }
            });
            btnRmv.setVisible(false);
            gpw.add(lblRole, 4, 0);
            gpw.add(btnClearRole, 0, 3);
        }
        else {
            btnRmv.setVisible(true);
        }

        btnRmv.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (ViewListenable l :
                        allListeners) {
                    message(l.removeWorker(w));
                }
                refreshWorkerGrid(gp);
                departmentView.quickRefresh();
            }

        });


        ComboBox<String> type = new ComboBox<>();
        type.getItems().addAll("Monthly", "Hourly", "Commission");
        type.getSelectionModel().select("Hourly");
        if(w.getType().equals("Monthly"))
            type.getSelectionModel().select("Monthly");
        if(w.getType().equals( "Hourly"))
            type.getSelectionModel().select("Hourly");
        if(w.getType().equals( "Commission"))
            type.getSelectionModel().select("Commission");

        Label lblBase = new Label("Month wage:");
        Label lblPerHour = new Label("Hour wage:");
        Label lblHourAMonth = new Label("Hours a month");
        Label lblRevenue = new Label("Revenue in sales:");
        Label lblCommission = new Label("Commission:");
        Label lblSalary = new Label("Salary:");
        Label lblMoney = new Label(w.getSalary() + "");
        TextField tfOne = new TextField();
        TextField tfTwo = new TextField();
        TextField tfThree = new TextField();

        tfOne.setMaxWidth(100);
        tfTwo.setMaxWidth(100);
        tfThree.setMaxWidth(100);

        lblBase.setVisible(false);
        lblCommission.setVisible(false);
        lblHourAMonth.setVisible(false);
        lblPerHour.setVisible(false);
        lblRevenue.setVisible(false);
        lblSalary.setVisible(false);
        lblMoney.setVisible(false);

        tfOne.setVisible(false);
        tfTwo.setVisible(false);
        tfThree.setVisible(false);
        if(w.getSalary() == 0) {
            if (w.getType().equals("Hourly")) {
                tfOne.setVisible(true);
                tfTwo.setVisible(true);
                lblHourAMonth.setVisible(true);
                lblPerHour.setVisible(true);
            }
            if (w.getType().equals("Monthly")) {
                tfOne.setVisible(true);
                lblBase.setVisible(true);
            }
            if (w.getType().equals("Commission")) {
                tfOne.setVisible(true);
                tfTwo.setVisible(true);
                tfThree.setVisible(true);
                lblBase.setVisible(true);
                lblCommission.setVisible(true);
                lblRevenue.setVisible(true);
            }
        }
        else {
            lblSalary.setVisible(true);
            lblMoney.setVisible(true);
        }

        type.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for(ViewListenable l: allListeners) {
                    if (w.getRole() == null) {
                        if (type.getValue().equals("Monthly"))
                            message(l.setWorker(w, 0));
                        if (type.getValue().equals("Hourly"))
                            message(l.setWorker(w, 0, 0));
                        if (type.getValue().equals("Commission"))
                            message(l.setWorker(w, 0, 0, 0));

                        refreshWorkerGrid(mainGP);
                    }
                    else {
                        message("You must Clear the role before changing it's sallary", "#FF0000");
                        if(w.getType().equals("Monthly"))
                            type.getSelectionModel().select("Monthly");
                        if(w.getType().equals( "Hourly"))
                            type.getSelectionModel().select("Hourly");
                        if(w.getType().equals( "Commission"))
                            type.getSelectionModel().select("Commission");
                    }
                }
            }
        });

        tfName.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                w.setName(tfName.getText());
                departmentView.quickRefresh();
            }
        });

        cboxHome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                w.setPreferWorkingHome(cboxHome.isSelected());
                departmentView.quickRefresh();
            }
        });

        hours.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                w.setPreferredStartingHour(hours.getValue());
                departmentView.quickRefresh();
            }
        });

        gpw.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    if(tfOne.getText() != "" || tfTwo.getText() != "" || tfThree.getText() != "") {
                        if (type.getValue().equals("Monthly")){
                            w.setSalary(new Salary(Float.parseFloat(tfOne.getText())));
                            departmentView.quickRefresh();
                        }
                        else if (type.getValue().equals("Hourly")) {
                            ((HourlyWorker) w).setSalary(Float.parseFloat(tfOne.getText()), Float.parseFloat(tfTwo.getText()));
                            departmentView.quickRefresh();
                        }
                        else if(Float.parseFloat(tfThree.getText()) > 1 || Float.parseFloat(tfThree.getText()) < 0 ){
                            message("Commission must be between 0 and 1", "#FF0000");
                        }
                        else if (type.getValue().equals("Commission")) {
                            w.setSalary(new Salary(Float.parseFloat(tfOne.getText()), Float.parseFloat(tfTwo.getText()), Float.parseFloat(tfThree.getText())));
                            departmentView.quickRefresh();
                        }
                    }
                }
                catch (Exception e){
                    message(new Message("must be int of float", true));
                }
            }
        });
        gpw.add(lblName, 0, 0);
        gpw.add(tfName, 1, 0);
        gpw.add(lblPreference, 0, 1);
        gpw.add(lblHour, 0, 2);
        gpw.add(hours, 1, 2);
        gpw.add(cboxHome, 1, 1);
        gpw.add(btnRmv, 0, 3);

        gpw.add(type, 3, 0);
        gpw.add(lblBase, 3, 1);
        gpw.add(lblPerHour, 3, 1);
        gpw.add(lblHourAMonth, 3, 2);
        gpw. add(lblRevenue, 3, 2);
        gpw.add(lblCommission, 3, 3);
        gpw.add(tfOne, 4, 1);
        gpw.add(tfTwo, 4, 2);
        gpw.add(tfThree, 4, 3);

        gpw.add(lblSalary, 3, 1);
        gpw.add(lblMoney, 4, 1);

        return gpw;
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
