package service.user;

import db.Database;
import http.request.HttpMethod;
import java.util.Map;
import model.User;
import service.Service;

public class OneUserSearchService extends Service {

    @Override
    public byte[] execute(HttpMethod method, Map<String, String> params, Map<String, String> body) {
        validate(method, params, body);

        User userEntity = getOneUserEntity(params.get("userId"));

        return userEntity.toString().getBytes();
    }

    @Override
    public void validate(HttpMethod method, Map<String, String> params, Map<String, String> body) {
        if (!method.equals(HttpMethod.GET)) {
            throw new IllegalArgumentException("method is not GET");
        }
        if (params.get("userId").isEmpty()) {
            throw new IllegalArgumentException("userId is empty");
        }
    }

    private User getOneUserEntity(String userId) {
        return Database.findUserById(userId);
    }

}
