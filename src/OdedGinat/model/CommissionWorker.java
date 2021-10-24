package OdedGinat.model;

import OdedGinat.exceptions.CommissionException;

import java.io.Serializable;

public class CommissionWorker extends Worker implements Serializable {
    private int hours = 160;
    private float revenue;
    private float base;
    private float commission;
    public CommissionWorker(String name, int startHour, Boolean preferHome, float base, float revenue, float commission) throws  Exception{
        super(name);
        super.setPreferWorkingHome(preferHome);
        if(commission < 0 || commission > 1)
            throw new CommissionException();

        super.setPreferredStartingHour(startHour);

        super.setSalary(new Salary(base, revenue, commission));
        this.revenue = revenue;
        this.base = base;
        this.commission = commission;
    }

    public CommissionWorker(String name, int startHour, Boolean preferHome, double base, double revenue, double commission) throws  Exception{
        super(name);
        super.setPreferWorkingHome(preferHome);
        if(commission < 0 || commission > 1)
            throw new CommissionException();

        if(!preferHome){
            super.setPreferredStartingHour(startHour);
        }
        super.setSalary(new Salary((float) base, (float) revenue, (float) commission));
        this.revenue = (float) revenue;
        this.base = (float) base;
        this.commission = (float) commission;
    }

    public CommissionWorker(Worker worker, float base, float revenue, float commission){
        super(worker);
        super.setPreferWorkingHome(worker.getPreferWorkingHome());

        super.setPreferredStartingHour(worker.getPreferredStartingHour());

        super.setSalary(new Salary(base, revenue, commission));

        this.revenue = revenue;
        this.base = base;
        this.commission = commission;
    }

    public void setSalary(float base, float revenue, float commission) {
        super.setSalary(new Salary(base, revenue, commission));
        this.revenue = revenue;
        this.base = base;
        this.commission = commission;
    }

    public float getRevenue() {
        return revenue;
    }

    public void setRevenue(float revenue) {
        this.revenue = revenue;
        super.setSalary(new Salary(base, revenue, commission));
    }

    public float getBase() {
        return base;
    }

    public float getCommission() {
        return commission;
    }

    public float getHours() {
        return hours;
    }

    @Override
    public String getType() {
        return "Commission";
    }
}
