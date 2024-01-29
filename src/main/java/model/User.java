package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User() {}

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password, name, email);
    }

    public static User of(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.userId = resultSet.getString("userId");
        user.password = resultSet.getString("password");
        user.name = resultSet.getString("name");
        user.email = resultSet.getString("email");
        return user;
    }
}
