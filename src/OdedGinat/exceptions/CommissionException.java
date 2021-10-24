package OdedGinat.exceptions;

public class CommissionException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid commission";
    }
}
