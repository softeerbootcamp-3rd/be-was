package service;

import db.Database;
import model.User;
import java.util.Map;


public class UserService {

    public boolean signUp(Map<String, String> queryParams){
        if (!validateParams(queryParams)){
            return false;
        }
        User newUser = createUser(queryParams);
        if (Database.findUserById(newUser.getUserId()) != null) {
            return false;
        }
        Database.addUser(newUser);
        return true;
    }

    private User createUser(Map<String, String> queryParams) {
        String userId = queryParams.getOrDefault("userId", "");
        String password = queryParams.getOrDefault("password", "");
        String name = queryParams.getOrDefault("name", "");
        String email = queryParams.getOrDefault("email", "");
        return new User(userId, password, name, email);
    }

    private boolean validateParams(Map<String, String> queryParams) {
        return queryParams.containsKey("userId") && !queryParams.get("userId").isEmpty()
                && queryParams.containsKey("password") && !queryParams.get("password").isEmpty()
                && queryParams.containsKey("name") && !queryParams.get("name").isEmpty()
                && queryParams.containsKey("email") && !queryParams.get("email").isEmpty();
    }

}
