package model;

import webserver.RequestHeader;

import java.lang.reflect.Field;

public class User {

    private String userId;
    private String password;
    private String name;
    private String email;

    public User() {
    }

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public static void setUser(User user, String key, String value) {
        Class<User> userClazz = User.class;
        try {
            Field declaredField = userClazz.getDeclaredField(key);
            declaredField.setAccessible(true);
            declaredField.set(user, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
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
}
