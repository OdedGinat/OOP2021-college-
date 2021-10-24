package OdedGinat.model;

import java.io.Serializable;

public class BaseSalaryWorker extends Worker implements Serializable {
    private int hours = 160;
    public BaseSalaryWorker(String name, int startHour, Boolean preferHome, float baseSalary){
        super(name);
        super.setPreferWorkingHome(preferHome);
        super.setPreferredStartingHour(startHour);

        super.setSalary(new Salary(baseSalary));
    }

    public BaseSalaryWorker(Worker worker, float baseSalary){
        super(worker);
        super.setPreferWorkingHome(worker.getPreferWorkingHome());
        super.setPreferredStartingHour(worker.getPreferredStartingHour());
        super.setSalary(new Salary(baseSalary));
    }

    public Boolean setSalary(float baseSalary) {
        super.setSalary(new Salary(baseSalary));
        return true;
    }

    public float getHours() {
        return hours;
    }

    @Override
    public String getType() {
        return "Monthly";
    }
}

