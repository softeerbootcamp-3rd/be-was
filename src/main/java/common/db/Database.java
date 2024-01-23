package common.db;

import com.google.common.collect.Maps;
import domain.user.command.domain.User;
import java.util.Map;

public class Database {

    private static final Map<String, User> users = Maps.newConcurrentMap();
    private static final Map<String, String> sessionStorage = Maps.newConcurrentMap();

    public static Map<String, User> Users() {
        return users;
    }

    public static Map<String, String> SessionStorage() {
        return sessionStorage;
    }
}
