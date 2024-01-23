package domain.user.command.application;

import common.http.response.HttpStatusCode;
import common.utils.ResponseUtils;
import common.utils.SessionGenerator;
import domain.user.command.domain.User;
import domain.user.command.domain.UserRepository;
import domain.user.infrastructure.UserRepositoryImpl;
import domain.user.presentation.UserLoginRequest;
import java.util.Optional;
import webserver.container.ResponseThreadLocal;

public class UserLoginService {
    private UserRepository userRepository;

    public UserLoginService() {
        userRepository = new UserRepositoryImpl();
    }

    public void login(UserLoginRequest userLoginRequest) {
        User user = checkExistUser(userLoginRequest);

        if (user == null || !user.getPassword().equals(userLoginRequest.getPassword())) {
            ResponseThreadLocal.onFailure(HttpStatusCode.FOUND, ResponseUtils.makeLoginFailedHeader(), new byte[0]);
            return;
        }

        String sessionId = mapUserToSessionStorageAndGetSessionId(user.getUserId());

        ResponseThreadLocal.onSuccess(HttpStatusCode.FOUND, ResponseUtils.makeLoginHeader(sessionId), new byte[0]);
    }

    private User checkExistUser(UserLoginRequest userLoginRequest) {
        Optional<User> optionalUser = userRepository.findUserById(userLoginRequest.getUserId());
        return optionalUser.orElse(null);
    }

    private String mapUserToSessionStorageAndGetSessionId(String userId) {
        Optional<String> sessionIdByUserId = userRepository.getSessionIdByUserId(userId);
        if (sessionIdByUserId.isPresent()) {
            return sessionIdByUserId.get();
        }
        String sessionId = SessionGenerator.generateSessionId();
        userRepository.saveSession(sessionId, userId);
        return sessionId;
    }
}
