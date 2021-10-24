package OdedGinat.model;

import java.io.Serializable;
import java.util.Objects;

public abstract class Worker implements Comparable, Serializable {
    private String name;
    private int preferredStartingHour;
    private Boolean preferWorkingHome;
    private Salary salary;
    private Role role = null;
    private static int intPool = 0;
    private int id;

    public Worker(String name){
        this.name = name;
        this.id = intPool;
        intPool++;
    }

    public Worker(String name, int preferredStartingHour){
        this.name = name;
        this.preferWorkingHome = false;
        this.preferredStartingHour = preferredStartingHour;
        this.id = intPool;
        intPool++;
    }

    public Worker(String name, Boolean preferWorkingHome){
        this.name = name;
        this.preferWorkingHome = preferWorkingHome;
        this.id = intPool;
        intPool++;
    }

    public Worker(String name, int preferredStartingHour, Boolean preferWorkingHome){
        this.name = name;
        this.preferredStartingHour = preferredStartingHour;
        this.preferWorkingHome = preferWorkingHome;
        this.id = intPool;
        intPool++;
    }

    public Worker(Worker w){
        this.name = w.name;
        this.preferredStartingHour = w.preferredStartingHour;
        this.preferWorkingHome = w.preferWorkingHome;
        this.id = w.id;
    }

    public Boolean setPreferredStartingHour(int preferredStartingHour) {
        this.preferredStartingHour = preferredStartingHour;
        return true;
    }

    public Boolean assignRole(Role role){
        if(this.role != null)
            this.role.clearRole();
        this.role = role;
        if(role.getRoleAssignment() != this){
            role.assignRole(this);
        }
        return true;
    }

    public Boolean clearRole(){
        this.role = null;
        return true;
    }

    public Boolean setName(String name) {
        this.name = name;
        return true;
    }

    public Boolean setPreferWorkingHome(Boolean preferWorkingHome) {
        this.preferWorkingHome = preferWorkingHome;
        return true;
    }

    public Role getRole(){
        return role;
    }

    public  String getName(){
        return name;
    }

    public Integer getPreferredStartingHour() {
//        if(preferWorkingHome){
//            return -1;
//        }
        return preferredStartingHour;
    }

    public int getId() {
        return id;
    }

    public Boolean getPreferWorkingHome(){
        return preferWorkingHome;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    public float getSalary(){
        return salary.getSalary();
    }

    public String getType() {
        return "";
    }

    public float getHours(){
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return id == worker.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(this.id, ((Worker) o).id);
    }

    public static void setIntPool(int num){
        if(num > intPool)
            intPool = num;
    }

    @Override
    public String toString() {
        return "ID: " + id + " Name: " + name;
    }
}
