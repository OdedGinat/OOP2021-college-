package OdedGinat.model;

import OdedGinat.exceptions.TimeException;

import java.io.Serializable;

public class Role implements Flexible, Syncable, Serializable {
    private String description;
    private Worker roleAssignment;
    private int startTime;
    private Boolean workFromHome;
    private Boolean isSynced;
    private Float productivityModifier = null;
    private Float monthlyRevenue;

    public Role(String description){
        this.description = description;
        isSynced = false;
        workFromHome = false;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Boolean setWorkConditions(int startTime, Boolean workFromHome) throws Exception {
        if(!isSynced) {
            TimeException te = TimeException.check(startTime);

            if (te != null) {
                throw te;
            }
            if (!workFromHome) {
                this.startTime = startTime;
            }
            this.workFromHome = workFromHome;
            return true;
        }
        return false;
    }

    public Boolean sync(int startTime, Boolean workFromHome) {
        isSynced = true;
        this.startTime = startTime;
        this.workFromHome = workFromHome;
//        setWorkConditions(startTime, workFromHome);
        return true;
    }

    public Boolean unSync(){
        isSynced = false;
        return true;
    }

    public Boolean assignRole(Worker worker){
        if(this.roleAssignment != null)
            this.roleAssignment.clearRole();
        this.roleAssignment = worker;
        if(worker.getRole() != this){
            worker.assignRole(this);
        }
        setProductivityModifier();
        return true;
    }

    public Boolean clearRole(){
        this.roleAssignment = null;
        return true;
    }

    public void setProductivityModifier() {
        int preferredHour = roleAssignment.getPreferredStartingHour();
        int[] day = new int[24];
        if(workFromHome ){
            if(roleAssignment.getPreferWorkingHome()) {
                productivityModifier = (float) 0.8; //0.1*8

                monthlyRevenue = (float) (roleAssignment.getHours() * 10 * 1.1);

            }
            else {
                productivityModifier = (float) 0;
                monthlyRevenue = roleAssignment.getHours() * 10;
            }
        }
        else {
            for(int i = 0; i < 9; i++){
                if(i != 5) {
                    if (startTime + i < 24) {
                        day[startTime + i] = -1;
                    } else {
                        day[(startTime + i) - 24] = -1;
                    }
                }
                else {
                    if (startTime + i < 24) {
                        day[startTime + i] = 0;
                    } else {
                        day[(startTime + i) - 24] = 0;
                    }
                }
            }
            for (int i = 8; i < 17; i++) {
                day[i] = 0;
            }
            for (int i = 0; i < 9; i++) {
                if(preferredHour+i < 24) {
                    day[preferredHour + i] *= -1;
                }
                else {
                    day[(preferredHour+i)-24] *= -1;
                }
            }

            productivityModifier = (float) 0.0;
            for (int i = 0; i < day.length; i++) {
                productivityModifier += (float) day[i];

            }
            monthlyRevenue = (roleAssignment.getHours() * 10) + (roleAssignment.getHours()/9) * productivityModifier*2;
        }
    }



    public Float getMonthlyRevenue(){
        if(roleAssignment == null)
            return (float) 0;
        setProductivityModifier();
        return monthlyRevenue;
    }

    public Float getProductivityModifier(){
        setProductivityModifier();
        return productivityModifier;
    }

    public String getDescription(){
        return this.description;
    }

    public Worker getRoleAssignment(){
        return roleAssignment;
    }

    public int getStartTime() {
        return startTime;
    }

    public Boolean getWorkFromHome() {
        return workFromHome;
    }

    @Override
    public Boolean isSyncable() {
        return isSynced;
    }

    @Override
    public String toString() {
        if(roleAssignment == null){
            return "(Empty Role) \r\n" +
                    "description: " + description + '|' +
                    "startTime: " + startTime + '|' +
                    "workFromHome: " + workFromHome + '|' +
                    "isSynced: " + isSynced;

        }
        return "(Role) \r\n" +
                "description: " + description + '|' +
                "roleAssignment: " + roleAssignment.getName() + '|' +
                "startTime: " + startTime + '|' +
                "workFromHome: " + workFromHome + '|' +
                "isSynced: " + isSynced + '|' +
                "work modifier: " + productivityModifier;
    }
}
