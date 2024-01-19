package service.user;

import db.Database;
import dto.user.request.UserCreateRequestDto;
import http.request.HttpMethod;
import java.util.Map;
import model.User;
import service.Service;

public class UserCreateService extends Service {

    @Override
    public byte[] execute(HttpMethod method, Map<String, String> params, Map<String, String> body) {
        validate(method, params, body);

        UserCreateRequestDto userCreateRequestDto = UserCreateRequestDto.of(params);
        checkDuplicateUserId(userCreateRequestDto.getUserId());

        User newUser = createUserEntity(userCreateRequestDto);
        saveUser(newUser);

        return newUser.toString().getBytes();
    }

    @Override
    public void validate(HttpMethod method, Map<String, String> params, Map<String, String> body) {
        if (!method.equals(HttpMethod.GET)) {
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

    private User createUserEntity(UserCreateRequestDto userCreateRequestDto) {
        return new User(
            userCreateRequestDto.getUserId(),
            userCreateRequestDto.getPassword(),
            userCreateRequestDto.getName(),
            userCreateRequestDto.getEmail());
    }

    private void checkDuplicateUserId(String userId) {
        if (Database.findUserById(userId) != null) {
            throw new IllegalArgumentException("userId is duplicate");
        }
    }

    private void saveUser(User user) {
        Database.addUser(user);
    }
}
