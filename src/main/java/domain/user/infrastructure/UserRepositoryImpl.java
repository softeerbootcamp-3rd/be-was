package domain.user.infrastructure;

import static common.db.Database.SessionStorage;
import static common.db.Database.Users;

import domain.user.command.domain.User;
import domain.user.command.domain.UserRepository;
import java.util.Collection;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public void addUser(User user) {
        Users().put(user.getUserId(), user);
    }

    @Override
    public Optional<User> findUserById(String userId) {
        return Optional.ofNullable(Users().get(userId));
    }

    @Override
    public Collection<User> findAll() {
        return Users().values();
    }

    @Override
    public void saveSession(String userId, String sessionId) {
        SessionStorage().put(userId, sessionId);
    }

    @Override
    public Optional<String> getSessionIdByUserId(String userId) {
        return Optional.ofNullable(SessionStorage().get(userId));
    }

    @Override
    public void removeSession(String sessionId) {
        SessionStorage().remove(sessionId);
    }
}
