package service;

import db.Database;
import java.util.Map;
import model.User;

public class UserService {

    /**
     * 쿼리 파라미터에서 사용자 정보를 사용해 데이터베이스에 저장합니다.
     *
     * <p> user Id, password, name 중 하나라도 비어있거나, 이미 등록된 user Id 라면 오류를 발생시킵니다.
     *
     * @param params 쿼리 파라미터 (Map)
     * @throws NullPointerException user Id, password, name 중 하나라도 비어있는 경우 발생
     * @throws IllegalArgumentException 동일한 아이디가 이미 등록되어있는 경우 발생
     */
    public void createUser(Map<String, String> params)
            throws NullPointerException, IllegalArgumentException {
        try {
            if (params.get("userId").isEmpty() || params.get("password").isEmpty() || params.get(
                    "name").isEmpty()) {
                throw new NullPointerException("cannot find params.");
            }
            User user = new User(params.get("userId"), params.get("password"), params.get("name"),
                    params.get("email"));
            Database.addUser(user);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("user id already exists.");
        }
    }

    /**
     * 로그인 정보가 올바르지 않은지 확인합니다.
     *
     * <p> 로그인 정보가 없거나 해당하는 사용자가 없다면 true를 반환합니다.
     *
     * @param params 로그인 정보 파라미터
     * @return 정보가 올바르면 false, 올바르지 않으면 true
     */
    public User findUser(Map<String, String> params) {
        try {
            String userId = params.get("userId");
            String password = params.get("password");
            if (userId.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException();
            }
            User user = Database.findUserById(userId);
            if (!user.getPassword().equals(password)) {
                throw new IllegalArgumentException();
            }
            return user;
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
    }
}
