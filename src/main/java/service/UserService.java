package service;

import db.Database;
import model.User;
import utils.SessionManager;

import java.util.*;


public class UserService {

    public boolean signUp(Map<String, String> bodyParams){
        if (!validateParams(bodyParams)){
            return false;
        }
        User newUser = createUser(bodyParams);
        if (Database.findUserById(newUser.getUserId()) != null) {
            return false;
        }
        Database.addUser(newUser);
        return true;
    }

    public String login(Map<String, String> bodyParams){
        String userId = bodyParams.getOrDefault("userId", "");
        String password = bodyParams.getOrDefault("password", "");
        if(!isExistUser(userId, password)){
            return null;
        }
        String sessionId = SessionManager.createSessionId();
        SessionManager.addSessionId(sessionId, userId);
        return sessionId;
    }

    public List<User> getLoginUsers(){
        Collection<String> sessionIds = SessionManager.findAllSessionIds();
        List<User> loggedInUsers = new ArrayList<>();

        for (String sessionId : sessionIds) {
            String userId = SessionManager.getUserId(sessionId);
            User user = Database.findUserById(userId);
            if (user != null) {
                loggedInUsers.add(user);
            }
        }

        return loggedInUsers;
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

    private boolean isExistUser(String userId, String password){
        User existUser = Database.findUserById(userId);
        return Optional.ofNullable(existUser).map(User::getPassword).filter(p -> p.equals(password)).isPresent();
    }
}
