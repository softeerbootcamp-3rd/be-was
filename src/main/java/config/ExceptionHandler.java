package config;

public class ExceptionHandler {
    public static class UserIdTaken extends RuntimeException {
        public UserIdTaken(String message) {
            super(message);
        }
    }

    public static class AlreadyLoggedIn extends RuntimeException {
        public AlreadyLoggedIn(String message) {
            super(message);
        }
    }



}
