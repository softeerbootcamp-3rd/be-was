package config;

public class ExceptionType {
    public static class UserIdTaken extends RuntimeException {
        public UserIdTaken(String message) {
            super(message);
        }
    }
}
