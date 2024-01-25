package service;

import db.SessionDatabase;
import db.UserDatabase;
import model.Session;
import model.User;

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
                // 사용자의 세션 정보 업데이트 후 세션 ID 반환
                return updateSessionAndReturnSessionId(userId);
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

    // 사용자 세션 정보 반환
    private String updateSessionAndReturnSessionId(String userId) {
        // 기존에 존재하는 유효한 Session 정보가 있는 경우 해당 Session ID 반환
        Session existedSession = SessionDatabase.findValidSessionByUserId(userId);

        if (existedSession != null) {
            return existedSession.getSessionId();
        }

        // 기존에 존재하는 유효한 Session 정보가 없는 경우 새롭게 만든 후 해당 Session ID 반환
        Session newSession = new Session(userId);
        return SessionDatabase.addSession(newSession);
    }
}
