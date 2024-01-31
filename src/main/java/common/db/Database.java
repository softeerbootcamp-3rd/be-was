package common.db;

import com.google.common.collect.Maps;
import domain.user.command.domain.User;
import domain.user.query.dto.UserInfo;
import java.util.Map;

public class Database {

    private static final Map<String, User> users = Maps.newConcurrentMap();
    private static final Map<String, UserInfo> userInfos = Maps.newConcurrentMap();
    private static final Map<String, String> sessionStorage = Maps.newConcurrentMap();

    public static Map<String, User> Users() {
        return users;
    }

    public static Map<String, UserInfo> UserInfos() {
        return userInfos;
    }
    public static Map<String, String> SessionStorage() {
        return sessionStorage;
    }

}
