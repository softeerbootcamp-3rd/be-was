package service;

import db.Database;
import model.User;
import util.SessionUtil;

import java.util.Map;

public class UserService {

    private static final int SESSION_ID_LENGTH = 8;

    public void createUser(Map<String, String> createUserParams) {
        String userId = createUserParams.get("userId");
        String password = createUserParams.get("password");
        String name = createUserParams.get("name");
        String email = createUserParams.get("email");

        if (userId != null && password != null && name != null && email != null) {
            Database.addUser(new User(userId, password, name, email));
        } else {
            throw new IllegalArgumentException("Invalid Parameters");
        }
    }

    public String loginUser(Map<String, String> loginUserParams) {
        String userId = loginUserParams.get("userId");
        String password = loginUserParams.get("password");

        if (userId != null && password != null) {
            User user = Database.findUserById(userId);
            if (user != null && user.getPassword().equals(password)) {
                // 세션 ID 생성
                String sessionId = SessionUtil.createSessionId(SESSION_ID_LENGTH);
                Database.updateUserSession(userId, sessionId);
                return sessionId;
            }
            throw new IllegalArgumentException("Invalid userId and password");
        } else {
            throw new IllegalArgumentException("Invalid Parameters");
        }
    }
}
