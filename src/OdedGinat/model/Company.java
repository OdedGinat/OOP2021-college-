package OdedGinat.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Vector;

public class Company implements Serializable {
    private String name;
    private Vector<Department> departmentVector;
    private Vector<Worker> workers;
    private Vector<Worker> assignedWorkers;
    private float monthlyRevenue;

    public Company(String name){
        this.name = name;
        this.departmentVector = new Vector<>();
        this.workers = new Vector<>();
        this.assignedWorkers = new Vector<>();
    }

    public Company(){
        this.name = "";
        this.departmentVector = new Vector<>();
        this.workers = new Vector<>();
        this.assignedWorkers = new Vector<>();
    }

    public Boolean addWorker(Worker worker){
//        for (Worker w :
//                workers) {
//            System.out.println(worker.getName() + worker.getPreferWorkingHome());
//        }
        this.workers.add(worker);
        Collections.sort(workers);
        return true;
    }

    public Boolean removeWorker(Worker worker){
        if(!workers.contains(worker))
            return false;
        workers.remove(worker);
        return true;
    }

    public Boolean assignWorker(Worker worker, Role role){
        worker.assignRole(role);
        return workers.remove(worker) && assignedWorkers.add(worker);
    }

    public Boolean unassignWorker(Worker worker, Role role){
        worker.clearRole();
        role.clearRole();
        return assignedWorkers.remove(worker) && workers.add(worker);
    }

    public Boolean addDepartment(Department department){
        this.departmentVector.add(department);
        return true;
    }

    public String getName() {
        return name;
    }

    public Vector<Department> getDepartmentVector() {
        return departmentVector;
    }

    public Vector<Worker> getWorkers() {
        return workers;
    }

    public Boolean setName(String name) {
        this.name = name;
        return true;
    }

    public Boolean removeDepartment(Department d){
        return departmentVector.remove(d);
    }

    public Vector<Worker> getAllWorkers(){
        Vector<Worker> all = new Vector<>();
        all.addAll(workers);
        all.addAll(assignedWorkers);
        Collections.sort(all);
        return all;
    }

    public float getMonthlyRevenue() {
        monthlyRevenue =0;
        if(departmentVector.size() == 0){
            return 0;
        }
        for(Department d : departmentVector){
            monthlyRevenue += d.getMonthlyRevenue();
        }
        return monthlyRevenue;
    }

    @Override
    public String toString() {
        String departments = "";
        for (Department d:
             departmentVector) {
            departments+=d.toString();
        }
        return "(Company) \r\n" +
                "name: " + name + '|' +
                "departmentVector: " + departments;
    }
}
