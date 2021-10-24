package OdedGinat.model;

import java.io.Serializable;

public class HourlyWorker extends Worker implements Serializable {
    private float hours;
    public HourlyWorker(String name, int startHour, Boolean preferHome, float hoursAMonth, float perHour){
        super(name);
        super.setPreferWorkingHome(preferHome);
        super.setPreferredStartingHour(startHour);

        this.hours = hoursAMonth;
        super.setSalary(new Salary(hoursAMonth, perHour));
    }

    public HourlyWorker(Worker worker,  float hoursAMonth, float perHour){
        super(worker);
        super.setPreferWorkingHome(worker.getPreferWorkingHome());
        super.setPreferredStartingHour(worker.getPreferredStartingHour());
        this.hours = hoursAMonth;
        super.setSalary(new Salary(hoursAMonth, perHour));
    }

    public Boolean setSalary(float perHour, float hours) {
        super.setSalary(new Salary(hours, perHour));
        this.hours = hours;
        return true;
    }

    @Override
    public String getType() {
        return "Hourly";
    }

    public float getHours() {
        return hours;
    }
}
