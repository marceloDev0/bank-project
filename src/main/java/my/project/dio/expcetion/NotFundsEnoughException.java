package my.project.dio.expcetion;

public class NotFundsEnoughException extends RuntimeException {
    public NotFundsEnoughException(String message) {
        super(message);
    }
}
