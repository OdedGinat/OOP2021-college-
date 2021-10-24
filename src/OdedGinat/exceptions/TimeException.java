package OdedGinat.exceptions;

public class TimeException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid hour";
    }

    public static TimeException check(int hour){
        if(hour < 0 || hour > 23)
            return new TimeException();
        else
            return null;
    }
}
