package OdedGinat.control;

import javafx.stage.Stage;
import OdedGinat.listeners.ViewListenable;
import OdedGinat.view.View;
import OdedGinat.model.*;
import java.io.*;
import java.util.Vector;

public class Controller implements ViewListenable {
    private View view;
    private Company company;

    public Controller(Stage stage){
        this.company = new Company();
        this.view = new View(stage, this);
    }

    public Controller(Stage stage, Company company){
        this.company = company;
        this.view = new View(stage, this);
    }

    @Override
    public void setCompany(String name) {
        this.company.setName(name);
    }

    @Override
    public Message addDepartment(String name) {
        company.addDepartment(new Department(name));
        return new Message("Department added "+ name, false);
    }

    @Override
    public Message addDepartment(Department department) {
        company.addDepartment(department);
        return new Message("Department added "+ department.getName(), false);
    }

    @Override
    public Message addRole(Department department, String name){
        department.addRole(new Role(name));
        return new Message("Role added " + name, false);
    }

    @Override
    public Message addRole(Department department, Role role) {
        department.addRole(role);
        return new Message("Role added " + role.getDescription(), false);
    }

    @Override
    public void refresh() {
        try {
            view.refresh();
        }
        catch (Exception e){
            view.message("Can't refresh: " + e.getMessage(), "#FF0000");
        }
    }

    @Override
    public Message addWorker(String name, int startHour, Boolean preferHome, float... parameters) {
        if(parameters.length == 1){
            company.addWorker(new BaseSalaryWorker(name, startHour, preferHome, parameters[0]));
        }
        else if(parameters.length == 2){
            company.addWorker(new HourlyWorker(name, startHour, preferHome, parameters[0], parameters[1]));
        }
        else if(parameters.length == 3){
            if(parameters[0]*parameters[1]*parameters[2] >= 0){
                return new Message("Must be Positive", true);
            }
            if(parameters[2] > 1){
                return new Message("Commission must be between 0 and 1");
            }
            try {
                company.addWorker(new CommissionWorker(name, startHour, preferHome, parameters[0], parameters[1], parameters[2]));
            } catch (Exception e) {
                return new Message("Commission must be between 0 and 1");
            }
        }
        else {
            return new Message("Must specify between 1 and 3 parameters", true);
        }
        return new Message("Added worker " +name, false);
    }

    public Message addWorker(String name, int startHour, Boolean preferHome, double... parameters) {
        if(parameters.length == 1){
            company.addWorker(new BaseSalaryWorker(name, startHour, preferHome, (float) parameters[0]));
        }
        else if(parameters.length == 2){
            company.addWorker(new HourlyWorker(name, startHour, preferHome, (float) parameters[0], (float) parameters[1]));
        }
        else if(parameters.length == 3){
            if(parameters[0]*parameters[1]*parameters[2] <= 0){
                return new Message("Must be Positive", true);
            }
            if(parameters[2] > 1){
                return new Message("Commission must be between 0 and 1");
            }
            try {
                company.addWorker(new CommissionWorker(name, startHour, preferHome, (float) parameters[0], (float) parameters[1], (float) parameters[2]));
            } catch (Exception e) {
                return new Message("Commission must be between 0 and 1");
            }
        }
        else {
            return new Message("Must specify between 1 and 3 parameters", true);
        }
        return new Message("Added worker " +name, false);
    }

    @Override
    public Message addWorker(Worker worker) {

        company.addWorker(worker);
        return new Message("Added worker " +worker.getName(), false);
    }

    @Override
    public Message removeWorker(Worker worker) {
        Boolean successes = company.removeWorker(worker);
        if(successes)
            return new Message("Removed " + worker.getName(), false);
        else
            return new Message(worker.getName() +" does not exist", true);
    }

    @Override
    public Company getCompany() {
        return company;
    }

    @Override
    public Vector<Department> getDepartments() {
        return company.getDepartmentVector();
    }

    @Override
    public Vector<Worker> getWorkers() {
        return company.getWorkers();
    }

    @Override
    public Vector<Worker> getAllWorkers() {
        return company.getAllWorkers();
    }

    @Override
    public Vector<Role> getRoles(Department d) {
        return d.getRoles();
    }

    @Override
    public String getCompanyName() {
        return company.getName();
    }

    @Override
    public float getMonthlyRevenue() {
        return company.getMonthlyRevenue();
    }

    @Override
    public Message setWorker(Worker worker, float... parameters) {
        if(parameters.length == 1){
            company.removeWorker(worker);
            BaseSalaryWorker w = new BaseSalaryWorker(worker, (float) parameters[0]);
            company.addWorker(w);
        }
        else if(parameters.length == 2){
            company.removeWorker(worker);
            HourlyWorker w = new HourlyWorker(worker, (float) parameters[0], (float) parameters[1]);
            company.addWorker(w);

        }
        else if(parameters.length == 3){
            company.removeWorker(worker);
            if(parameters[0]*parameters[1]*parameters[2] < 0){
                return new Message("Must be Positive", true);
            }
            if(parameters[2] > 1){
                return new Message("Commission must be between 0 and 1");
            }
            CommissionWorker w = new CommissionWorker(worker, (float) parameters[0], (float) parameters[1], (float) parameters[2]);
            company.addWorker(w);
        }
        else {
            return new Message("Must specify between 1 and 3 parameters", true);
        }
        return new Message("updated worker " +worker.getName(), false);
    }

    @Override
    public Message removeDepartment(Department d) {
        Boolean b = company.removeDepartment(d);
        if(b)
            return new Message("Removed " + d.getName(), !b);
        else
            return new Message("Could not remove " + d.getName(), !b);
    }

    @Override
    public Message assignWorker(Worker worker, Role role) {
        Boolean b = company.assignWorker(worker, role);
        if(b)
            return new Message("Assigned " + worker.getName(), b);
        return new Message("did not assign " + worker.getName(), b);
    }

    @Override
    public Message unassignWorker(Worker worker) {
        Boolean b = company.unassignWorker(worker, worker.getRole());
        if(b)
            return new Message("Unassigned " + worker.getName(), b);
        return new Message("did not unassign " + worker.getName(), b);
    }

    @Override
    public void save()
            throws FileNotFoundException, IOException {
        ObjectOutputStream f = new ObjectOutputStream(
                new FileOutputStream("saved.data"));
        f.writeObject(company);
        f.close();
    }

    @Override
    public void load()
            throws FileNotFoundException, IOException, ClassNotFoundException{
        ObjectInputStream f = new ObjectInputStream(new FileInputStream("saved.data"));
        Object o =  f.readObject();
        f.close();
        company = (Company) o;
    }
}
