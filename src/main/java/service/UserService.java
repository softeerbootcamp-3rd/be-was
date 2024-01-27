package service;

import db.SessionDatabase;
import db.UserDatabase;
import model.Session;
import model.User;
import util.SessionUtil;

import java.util.Map;

public class UserService {

    public void createUser(Map<String, String> createUserParams) {
        String userId = createUserParams.get("userId");
        String password = createUserParams.get("password");
        String name = createUserParams.get("name");
        String email = createUserParams.get("email");

        if (userId != null && password != null && name != null && email != null) {
            UserDatabase.addUser(new User(userId, password, name, email));
        } else {
            throw new IllegalArgumentException("Invalid Parameters");
        }
    }

    // 사용자의 로그인 정보가 유효한 지 판단한 후 세션 ID 반환
    public String loginUser(Map<String, String> loginUserParams) {
        String userId = loginUserParams.get("userId");
        String password = loginUserParams.get("password");

        if (userId != null && password != null) {
            // 사용자가 입력한 로그인 정보 확인
            if (validateLogin(userId, password)) {
                // 로그인 정보가 유효한 경우 세션을 만들고 session Id 리턴
                return createSession(userId);
            }
            throw new IllegalArgumentException("Invalid userId and password");
        } else {
            throw new IllegalArgumentException("Invalid Parameters");
        }
    }

    // 사용자가 입력한 로그인 정보가 유효한지 확인
    private boolean validateLogin(String userId, String password) {
        User user = UserDatabase.findUserById(userId);
        return user != null && user.getPassword().equals(password);
    }

    // 사용자에 해당하는 세션을 만들고 session Id 리턴
    private String createSession(String userId) {
        Session newSession = new Session(userId);
        return SessionDatabase.addSession(newSession);
    }
}
