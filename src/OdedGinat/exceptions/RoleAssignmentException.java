package OdedGinat.exceptions;

public class RoleAssignmentException extends Exception {
    @Override
    public String getMessage() {
        return "unable to assign role";
    }
}
