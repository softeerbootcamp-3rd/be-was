package domain.user.query.dao;

import domain.user.query.dto.UserInfo;
import java.util.List;
import java.util.Optional;

public interface UserInfoDao {
    void addUserInfo(UserInfo userInfo);

    Optional<UserInfo> findUserInfoById(String userId);

    List<UserInfo> findAllUserInfo();
}
