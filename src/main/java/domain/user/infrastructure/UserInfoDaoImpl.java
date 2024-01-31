package domain.user.infrastructure;

import static common.db.Database.UserInfos;

import domain.user.query.dao.UserInfoDao;
import domain.user.query.dto.UserInfo;
import java.util.List;
import java.util.Optional;

public class UserInfoDaoImpl implements UserInfoDao {

    @Override
    public void addUserInfo(UserInfo userInfo) {
        UserInfos().put(userInfo.getUserId(), userInfo);
    }

    @Override
    public Optional<UserInfo> findUserInfoById(String userId) {
        return Optional.ofNullable(UserInfos().get(userId));
    }

    @Override
    public List<UserInfo> findAllUserInfo() {
        return List.copyOf(UserInfos().values());
    }
}
