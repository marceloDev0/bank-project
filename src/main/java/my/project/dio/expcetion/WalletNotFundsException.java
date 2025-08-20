package my.project.dio.expcetion;

public class WalletNotFundsException extends RuntimeException {
    public WalletNotFundsException(String message) {
        super(message);
    }
}
