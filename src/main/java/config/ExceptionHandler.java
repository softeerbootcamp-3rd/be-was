package config;

public class ExceptionHandler {
    public static class UserIdTaken extends RuntimeException {
        public UserIdTaken(String message) {
            super(message);
        }
    }

}
