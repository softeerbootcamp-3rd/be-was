package domain.user.infrastructure;

import common.logger.CustomLogger;
import common.utils.AsyncExecutor;
import domain.user.command.domain.User;
import domain.user.query.dto.UserInfo;
import java.util.concurrent.CompletableFuture;

public class QueryCommandHandler {
    private UserRepositoryImpl userRepository;

    private UserInfoDaoImpl userInfoDao;

    public QueryCommandHandler() {
        userRepository = new UserRepositoryImpl();
        userInfoDao = new UserInfoDaoImpl();
    }

    public void addSaveUserInfoEventCommand(User user) {
        AsyncExecutor.getInstance().addEvent(() -> {
            CompletableFuture.supplyAsync(() -> {
                CustomLogger.printInfo("save user info");
                userInfoDao.addUserInfo(new UserInfo(user.getUserId(), user.getName(), user.getEmail()));
                return userInfoDao.findUserInfoById(user.getUserId());
            }).thenAccept(optionalUserInfo -> {
                if (optionalUserInfo.isEmpty()) {
                    userRepository.deleteUser(user.getUserId());
                    CustomLogger.printError(new RuntimeException("UserInfo is not saved"));
                }
            });
        });
    }
}
