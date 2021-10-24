package OdedGinat.view;

import OdedGinat.control.Controller;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import OdedGinat.listeners.ViewListenable;
import OdedGinat.model.Company;
import OdedGinat.model.Message;
import OdedGinat.model.Worker;

import java.util.Vector;

public class View {
    private Vector<ViewListenable> allListeners;

    private BorderPane bpMain;

    private Label lblErrors;
    private VBox errorVbox;

    private Stage primaryStage;

    private DepartmentView departmentView;
    private WorkersView workersView;

    private Button btnLoad = new Button("Load from file");;
    private Button btnSave = new Button("Save");;

    public View(Stage primaryStage, Controller controller){
        this.primaryStage = primaryStage;
        primaryStage.setMaximized(true);
        allListeners = new Vector<>();
        bpMain = new BorderPane();
        registerListeners(controller);
        errorBox();
        loadSave(btnLoad, btnSave);
        if(getCompany().getName().equals("")) {
            primaryStage.setTitle("Company initiation");
            newCompany();
        }
        else {
            companyGrid();
        }



        Scene scene = new Scene(bpMain, 1000, 600); // setting the new window
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void companyGrid(){
        String name = "";
        for (ViewListenable l:
             allListeners) {
            name = l.getCompanyName();
        }
        primaryStage.setTitle(name + " experiment");

        GridPane gpDepartment = new GridPane();
        DepartmentView departmentView = departmentGrid(gpDepartment);
        GridPane gpWorkers = new GridPane();
        WorkersView workersView = workerGrid(gpWorkers);
        workersView.setDepartmentView(departmentView);
        departmentView.setWorkersView(workersView);

        this.departmentView = departmentView;
        this.workersView = workersView;
    }

    public void refresh(){
        departmentView.quickRefresh();
        workersView.quickRefresh();
    }

    public DepartmentView departmentGrid(GridPane gpDepartment) {
        gpDepartment.setPadding(new Insets(10));
        gpDepartment.setHgap(10);
        gpDepartment.setVgap(10);
        ScrollPane sPane = new ScrollPane();
        DepartmentView departmentView = new DepartmentView(this, allListeners);
        departmentView.refreshDepartmentGrid(gpDepartment);
        sPane.setContent(gpDepartment);
        bpMain.setCenter(sPane);
        return departmentView;
    }

    public WorkersView workerGrid(GridPane gpWorkers){
        gpWorkers.setPadding(new Insets(10));
        gpWorkers.setHgap(10);
        gpWorkers.setVgap(10);
        ScrollPane sPane = new ScrollPane();
        WorkersView workersView = new WorkersView(this, allListeners);
        workersView.refreshWorkerGrid(gpWorkers);
        sPane.setContent(gpWorkers);
        bpMain.setRight(sPane);
        return workersView;
    }



    public void newCompany(){
        GridPane gpComp = new GridPane();
        Label lblName = new Label("Company name:");
        Button btnName = new Button("Register");
        TextField tfName = new TextField();

        gpComp.setPadding(new Insets(10));
        gpComp.setHgap(10);
        gpComp.setVgap(10);

        gpComp.add(btnLoad, 1, 1);
        gpComp.add(lblName, 0, 0);
        gpComp.add(btnName, 0, 1);
        gpComp.add(tfName, 1, 0);

        btnName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(areEmpty(tfName)){
                    message("Field must be filled", "#FF0000");
                }
                else{
                    message("Registered Company", "#0000FF");
                    for (ViewListenable l:
                         allListeners) {
                        l.setCompany(tfName.getText());
                    }
                    companyGrid();
                }
            }
        });

        bpMain.setCenter(gpComp);
    }

    public void loadSave(Button load, Button save){
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (ViewListenable l: allListeners) {
                    try {
                        l.load();
                        message("file loaded", "#0000ff");
                        for(Worker w : l.getAllWorkers())
                            Worker.setIntPool(w.getId()+1);
                        companyGrid();
                    }
                    catch (Exception e){
                        message("Could not load file " + e.toString(), "#FF0000");
                    }
                }
            }
        });
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (ViewListenable l: allListeners) {
                    try {
                        l.save();
                        message("saved", "#0000ff");
                    }
                    catch (Exception e){
                        message("Could not save file " + e.toString(), "#FF0000");
                    }
                }
            }
        });
    }

    public void registerListeners(ViewListenable l){
        allListeners.add(l);
    }

    private Company getCompany(){
        for (ViewListenable l:
             allListeners) {
            return l.getCompany();
        }
        return null;
    }

    public Boolean areEmpty(TextField... textFields){
        for (TextField tf:
             textFields) {
            if(tf.getText() == ""){
                return true;
            }
        }
        return false;
    }

    public void errorBox(){
        lblErrors = new Label("Okay");
        lblErrors.setFont(new Font("Ariel", 16));

        errorVbox = new VBox();
        errorVbox.setAlignment(Pos.BOTTOM_LEFT);
        errorVbox.setSpacing(10);
        errorVbox.setPadding(new Insets(5));
        errorVbox.getChildren().add(lblErrors);
        errorVbox.getChildren().add(btnSave);
        bpMain.setBottom(errorVbox);
    }

    public void message(String s, String color){
        lblErrors.setTextFill(Color.web("#000000", 0.8));
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.1), lblErrors);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setCycleCount(2);
        fadeTransition.play();
        lblErrors.setText(s);
        lblErrors.setTextFill(Color.web(color, 0.8));
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
