package model;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private User(Builder builder){
        this.userId = builder.userId;
        this.password = builder.password;
        this.name = builder.name;
        this.email = builder.email;
    }

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

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }

    public static class Builder{
        private String userId;
        private String password;
        private String name;
        private String email;

        public Builder(){}

        public Builder userId(String userId){
            this.userId = userId;

            return this;
        }

        public Builder password(String password){
            this.password = password;

            return this;
        }

        public Builder name(String name){
            this.name = name;

            return this;
        }

        public Builder email(String email){
            this.email = email;

            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}
