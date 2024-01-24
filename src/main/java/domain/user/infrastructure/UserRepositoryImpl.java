package domain.user.infrastructure;

import static common.db.Database.Users;

import domain.user.command.domain.User;
import domain.user.command.domain.UserRepository;
import java.util.Collection;

public class UserRepositoryImpl implements UserRepository {
    public void addUser(User user) {
        Users().put(user.getUserId(), user);
    }

    public User findUserById(String userId) {
        return Users().get(userId);
    }

    public Collection<User> findAll() {
        return Users().values();
    }
}
