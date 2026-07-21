public class InvalidOptionException extends ApplicationException {
    public InvalidOptionException() {
        super("Poll must contain at least 2 option");
    }
}
