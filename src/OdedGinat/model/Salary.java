package OdedGinat.model;

import java.io.Serializable;

public class Salary implements Serializable {
    private Float amount;

    public Salary(float baseSalary){
        this.amount = baseSalary;
    }

    public Salary(float monthlyHours, float payPerHour){
        this.amount = monthlyHours * payPerHour;
    }

    public Salary(float baseSalary, float monthlyRevenue, float commission){
        this.amount = baseSalary + (monthlyRevenue * commission);
    }

    public float getSalary(){
        return amount;
    }

    @Override
    public String toString() {
        return amount.toString();
    }
}
