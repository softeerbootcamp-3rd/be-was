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
    public void saveUser(Map<String, String> params)
            throws NullPointerException, IllegalArgumentException {
        try {
            if (params.get("userId").isEmpty() || params.get("password").isEmpty() || params.get(
                    "name").isEmpty()) {
                throw new NullPointerException();
            }
            User user = new User(params.get("userId"), params.get("password"), params.get("name"),
                    params.get("email"));
            Database.addUser(user);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }
}
