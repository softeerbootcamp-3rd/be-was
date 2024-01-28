package domain.user.infrastructure;

import static common.db.Database.SessionStorage;
import static common.db.Database.Users;

import domain.user.command.domain.User;
import domain.user.command.domain.UserRepository;
import java.util.Collection;
import java.util.Map.Entry;
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
    public void saveSession(String sessionId, String userId) {
        SessionStorage().put(sessionId, userId);
    }

    @Override
    public Optional<String> getSessionIdByUserId(String userId) {
        return SessionStorage().entrySet().stream()
            .filter(entry -> entry.getValue().equals(userId))
            .map(Entry::getKey)
            .findFirst();
    }

    @Override
    public void removeSession(String sessionId) {
        SessionStorage().remove(sessionId);
    }
}
