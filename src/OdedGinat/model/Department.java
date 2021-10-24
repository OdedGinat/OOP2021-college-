package OdedGinat.model;

import java.io.Serializable;
import java.util.Vector;

public class Department implements Syncable, Serializable {
    private String name;
    private Vector<Role> roles;
    private Boolean isSyncable;
    private int syncedTime;
    private Boolean syncedHome;
    private float monthlyRevenue;

    public Department(String name){
        this.name = name;
        this.isSyncable = false;
        this.roles = new Vector<>();
    }

    public Boolean addRole(Role role){
        roles.add(role);
        return true;
    }

    public Boolean removeRole(Role role){
        return roles.remove(role);
    }

    public Boolean setName(String name) {
        this.name = name;
        return true;
    }

    public Boolean setSyncConditions(int syncedTime, Boolean syncedHome){
        if(isSyncable) {
            this.syncedHome = syncedHome;
            this.syncedTime = syncedTime;
        }
        for(Role r : roles){
            try {
                r.sync(syncedTime, syncedHome);
            } catch (Exception e) {
                return false;
            }
        }
        return isSyncable;
    }

    @Override
    public Boolean sync(int startTime, Boolean workFromHome) throws Exception {
        isSyncable = true;
        for (Role r:
             roles) {
            r.sync(startTime, workFromHome);
        }
        syncedTime = startTime;
        syncedHome = workFromHome;
        return true;
    }

    @Override
    public Boolean unSync() {
        this.isSyncable = false;
        for (Role r:
                roles) {
            r.unSync();
        }
        return true;
    }

    public Boolean getSyncedHome() {
        return syncedHome;
    }

    public int getSyncedTime() {
        return syncedTime;
    }

    public Float getMonthlyRevenue() {
        monthlyRevenue = 0;
        if(roles.size() == 0){
            return (float)0;
        }
        for(Role r : roles){
            monthlyRevenue += r.getMonthlyRevenue();
        }
        return monthlyRevenue;
    }

    public String getName() {
        return name;
    }

    public Vector<Role> getRoles() {
        return roles;
    }

    public Vector<String> getRoleNames(){
        Vector<String> roleNames = new Vector<>();
        for (Role r:
                roles) {
            roleNames.add(r.getDescription());
        }
        return roleNames;
    }

    @Override
    public Boolean isSyncable() {
        return isSyncable;
    }

    @Override
    public String toString() {
        String roleNames = "";
        for (Role r:
             roles) {
            roleNames += r.toString() + "\r\n";
        }
        return "(Department) \r\n" +
                "name: " + name + '|' +
                "roles: " + roleNames;
    }
}
