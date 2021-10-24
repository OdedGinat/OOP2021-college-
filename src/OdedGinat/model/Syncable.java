package OdedGinat.model;

public interface Syncable {
    public Boolean isSyncable();
    public Boolean sync(int startTime, Boolean workFromHome) throws Exception;
    public Boolean unSync();
}
