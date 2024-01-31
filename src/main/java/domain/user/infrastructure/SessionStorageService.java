package domain.user.infrastructure;

import static common.db.Database.SessionStorage;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

public class SessionStorageService {
    public void saveSession(String sessionId, String userId) {
        SessionStorage().put(sessionId, userId);
    }

    public Optional<String> getSessionIdByUserId(String userId) {
        return SessionStorage().entrySet().stream()
            .filter(entry -> entry.getValue().equals(userId))
            .map(Entry::getKey)
            .findFirst();
    }

    public void removeSession(String sessionId) {
        SessionStorage().remove(sessionId);
    }

    public Optional<String> getUserIdBySessionId(String sessionId) {
        return Optional.ofNullable(SessionStorage().get(sessionId));
    }

    public List<String> getAllUserId() {
        return List.copyOf(SessionStorage().values());
    }
}
