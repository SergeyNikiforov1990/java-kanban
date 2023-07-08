package exceptions;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String message) {
        super();
        System.out.println(message);
    }
}
