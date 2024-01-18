package service;

import db.Database;
import dto.UserDto;
import java.util.Map;
import model.User;

public class UserService {

    public void saveUser(UserDto userDto) {
        User user = new User(userDto.getUserId(), userDto.getPassword(), userDto.getName(),
                userDto.getEmail());
        Database.addUser(user);
    }

    public void saveUser(Map<String, String> params) throws NullPointerException, IllegalArgumentException {
        try {
            User user = new User(params.get("userId"), params.get("password"), params.get("name"),
                    params.get("email"));
            Database.addUser(user);
        } catch (NullPointerException e) {
            throw new NullPointerException();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }
}
