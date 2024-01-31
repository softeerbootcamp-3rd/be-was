package domain.user.command.domain;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    public void addUser(User user);

    public Optional<User> findUserById(String userId);

    public Collection<User> findAll();

    public void deleteUser(String userId);
}
