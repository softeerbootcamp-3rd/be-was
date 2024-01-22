package domain.user.command.application;

import common.http.response.HttpStatusCode;
import common.utils.ResponseUtils;
import domain.user.command.domain.User;
import domain.user.command.domain.UserRepository;
import domain.user.infrastructure.UserRepositoryImpl;
import java.util.HashMap;
import webserver.container.ResponseThreadLocal;

public class UserCreateService {
    private UserRepository userRepository;

    public UserCreateService() {
        userRepository = new UserRepositoryImpl();
    }

    public void makeNewUser(UserCreateRequest userCreateRequest) {
        validate(userCreateRequest);

        if (checkDuplicateUserId(userCreateRequest.getUserId())) {
            ResponseThreadLocal.onFailure(HttpStatusCode.BAD_REQUEST, new HashMap<>(), "userId is duplicate".getBytes());
            return;
        };

        User newUser = createUserEntity(userCreateRequest);
        saveUser(newUser);

        ResponseThreadLocal.onSuccess(HttpStatusCode.MOVED_PERMANENTLY, ResponseUtils.makeRedirection("/index.html"), new byte[0]);
    }

    public void validate(UserCreateRequest userCreateRequest) {
        if (userCreateRequest.getUserId() == null) {
            ResponseThreadLocal.onFailure(HttpStatusCode.BAD_REQUEST, null, "userId is null".getBytes());
            return;
        }

        if (userCreateRequest.getPassword() == null) {
            ResponseThreadLocal.onFailure(HttpStatusCode.BAD_REQUEST, null, "password is null".getBytes());
            return;
        }

        if (userCreateRequest.getName() == null) {
            ResponseThreadLocal.onFailure(HttpStatusCode.BAD_REQUEST, null, "name is null".getBytes());
            return;
        }

        if (userCreateRequest.getEmail() == null) {
            ResponseThreadLocal.onFailure(HttpStatusCode.BAD_REQUEST, null, "email is null".getBytes());
            return;
        }
    }

    private User createUserEntity(UserCreateRequest userCreateRequest) {
        return new User(
            userCreateRequest.getUserId(),
            userCreateRequest.getPassword(),
            userCreateRequest.getName(),
            userCreateRequest.getEmail());
    }

    private boolean checkDuplicateUserId(String userId) {
        return userRepository.findUserById(userId) != null;
    }

    private void saveUser(User user) {
        userRepository.addUser(user);
    }
}
