package OdedGinat;

import OdedGinat.control.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import OdedGinat.model.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller controller = new Controller(primaryStage, new Company("Movies"));
        Department harryPotter = new Department("Harry Potter");

        controller.addDepartment(harryPotter);
        Worker danielRadcliffe = new BaseSalaryWorker("Daniel radcliffe", 4, false, 10000);

        controller.addWorker(danielRadcliffe);
        Worker emmaWatson = new CommissionWorker("Emma Watson", 10, false, 10000, 1000, 0.2);
        controller.addWorker(emmaWatson);
        Worker rupertGrint = new HourlyWorker("Rupert Grint", 8, true, 60, 165);
        controller.addWorker(rupertGrint);

        Role harry = new Role("Harry");
        Role hermione = new Role("Hermione");
        Role ron = new Role("Ron");

        controller.addRole(harryPotter, harry);
        controller.addRole(harryPotter, hermione);
        controller.addRole(harryPotter, ron);

        controller.assignWorker(danielRadcliffe, harry);
        controller.assignWorker(rupertGrint, ron);
        controller.assignWorker(emmaWatson, hermione);

        harryPotter.unSync();

        harryPotter.sync(8, false);

//
        controller.refresh();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
