package service.user;

import db.Database;
import java.util.Map;
import logger.CustomLogger;
import model.User;
import service.Service;

public class UserCreateService extends Service {

    @Override
    public byte[] execute(String method, Map<String, String> params, Map<String, String> body) {
        validate(method, params, body);
        User userEntity = createUserEntity(params, body);
        saveUser(userEntity);
        CustomLogger.printInfo(userEntity.toString());
        return userEntity.toString().getBytes();
    }

    @Override
    public void validate(String method, Map<String, String> params, Map<String, String> body) {
        if (!method.equals("GET")){
            throw new IllegalArgumentException("method is not GET");
        }
        if (params.get("userId").isEmpty()) {
            throw new IllegalArgumentException("userId is empty");
        }
        if (params.get("password").isEmpty()) {
            throw new IllegalArgumentException("password is empty");
        }
        if (params.get("name").isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        if (params.get("email").isEmpty()) {
            throw new IllegalArgumentException("email is empty");
        }
    }

    private User createUserEntity(Map<String, String> params, Map<String, String> body) {
        String userId = params.get("userId");
        String password = params.get("password");
        String name = params.get("name");
        String email = params.get("email");
        return new User(userId, password, name, email);
    }

    private void saveUser(User user) {
        if (Database.findUserById(user.getUserId()) != null) {
            throw new IllegalArgumentException("user already exists");
        }
        Database.addUser(user);
    }
}
