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

        if (checkDuplicateUserId(userCreateRequest.getUserId())) {
            ResponseThreadLocal.onFailure(HttpStatusCode.BAD_REQUEST, new HashMap<>(), "userId is duplicate".getBytes());
            return;
        };

        User newUser = createUserEntity(userCreateRequest);
        saveUser(newUser);

        ResponseThreadLocal.onSuccess(HttpStatusCode.FOUND, ResponseUtils.makeRedirection("/index.html"), new byte[0]);
    }

    private User createUserEntity(UserCreateRequest userCreateRequest) {
        return new User(
            userCreateRequest.getUserId(),
            userCreateRequest.getPassword(),
            userCreateRequest.getName(),
            userCreateRequest.getEmail());
    }

    private boolean checkDuplicateUserId(String userId) {
        return userRepository.findUserById(userId).isPresent();
    }

    private void saveUser(User user) {
        userRepository.addUser(user);
    }
}
