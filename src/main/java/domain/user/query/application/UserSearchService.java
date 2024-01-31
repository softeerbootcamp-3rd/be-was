package domain.user.query.application;

import common.http.response.HttpStatusCode;
import common.utils.ResponseUtils;
import domain.user.infrastructure.SessionStorageService;
import domain.user.infrastructure.UserInfoDaoImpl;
import domain.user.query.dao.UserInfoDao;
import domain.user.query.dto.UserInfo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import webserver.container.CustomThreadLocal;

public class UserSearchService {
    private UserInfoDao userInfoDao;
    private SessionStorageService sessionStorageService;

    public UserSearchService() {
        userInfoDao = new UserInfoDaoImpl();
        sessionStorageService = new SessionStorageService();
    }

    public void getMyUserInfo(String sessionId) {
        Optional<String> userId = sessionStorageService.getUserIdBySessionId(sessionId);
        if (userId.isEmpty()) {
            CustomThreadLocal.onFailure(HttpStatusCode.FOUND, ResponseUtils.makeRedirection("/user/login.html"),new byte[0]);
            return;
        }

        Optional<UserInfo> userInfo = userInfoDao.findUserInfoById(userId.get());
        if (userInfo.isEmpty()) {
            CustomThreadLocal.onFailure(HttpStatusCode.FOUND, ResponseUtils.makeRedirection("/user/form.html"),new byte[0]);
            return;
        }

        UserNameResponse userNameResponse = new UserNameResponse(userInfo.get().getUserName());
        CustomThreadLocal.onSuccess(
            HttpStatusCode.OK,
            ResponseUtils.makeJsonResponseHeader(),
            UserResponseToJsonConverter.userNameResponseConvertToJson(userNameResponse).getBytes());
    }

    public void getSignupUserInfos() {
        List<UserInfo> allUserInfo = userInfoDao.findAllUserInfo();

        List<UserInfoResponse> userInfoResponseList = allUserInfo.stream()
            .map(userInfo -> new UserInfoResponse(userInfo.getUserId(), userInfo.getUserName(), userInfo.getEmail()))
            .collect(Collectors.toList());


        CustomThreadLocal.onSuccess(
            HttpStatusCode.OK,
            ResponseUtils.makeJsonResponseHeader(),
            UserResponseToJsonConverter.userInfoResponseConvertToJson(userInfoResponseList).getBytes());
    }
}
