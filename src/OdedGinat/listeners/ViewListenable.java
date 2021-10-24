package OdedGinat.listeners;

import OdedGinat.model.*;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

public interface ViewListenable {
    public void setCompany(String name);
    public Vector<Department> getDepartments();
    public Vector<Role> getRoles(Department d);
    public Vector<Worker> getWorkers();
    public Vector<Worker> getAllWorkers();
    public String getCompanyName();
    public Company getCompany();


    public Message addDepartment(String name);
    public Message addDepartment(Department department);
    public Message removeDepartment(Department d);
    public Message addRole(Department department, String name);
    public Message addRole(Department department, Role role);
    public Message addWorker(String name, int startHour, Boolean preferHome, float... parameters);
    public Message addWorker(Worker worker);
    public Message removeWorker(Worker worker);
    public Message setWorker(Worker worker, float... parameters);
    public Message assignWorker(Worker worker, Role role);
    public Message unassignWorker(Worker worker);
    public void refresh();
    public float getMonthlyRevenue();

    public void load() throws FileNotFoundException, IOException, ClassNotFoundException;
    public void save() throws FileNotFoundException, IOException, ClassNotFoundException;
}
