package dto;

public class UserRequest {
    public static class Register{
        private String userId;
        private String password;
        private String name;
        private String email;

        public Register(){}

        public String getUserId() {
            return userId;
        }

        public String getPassword() {
            return password;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}
