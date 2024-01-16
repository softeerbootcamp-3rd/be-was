package model;

import java.lang.reflect.Field;

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

    public User(String params) {
        try {
            for (String param : params.split("&")) {
                String[] keyAndValue = param.split("=");
                String key = keyAndValue[0];
                String value = keyAndValue[1];

                Field field = this.getClass().getDeclaredField(key);
                field.setAccessible(true);
                field.set(this, value);
            }
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
