package domain.user.command.domain;

import java.util.Collection;

public interface UserRepository {
    public void addUser(User user);

    public User findUserById(String userId);

    public Collection<User> findAll();
}
