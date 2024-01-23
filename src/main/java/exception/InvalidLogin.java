package exception;

public class InvalidLogin extends BadRequestException {

    public InvalidLogin(String message) {
        super(message);
    }

    public InvalidLogin(String message, Throwable cause) {
        super(message, cause);
    }
}
