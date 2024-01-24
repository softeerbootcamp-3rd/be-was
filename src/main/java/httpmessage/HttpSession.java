package httpmessage;

import model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSession {
    private String id;
    private static Map<String, User> values = new HashMap<>();
    public HttpSession() {}
    public HttpSession(User user){
        id = UUID.randomUUID().toString();
        values.put(id,user);
    }
    public User getUser(String sid) {
        return values.get(sid);
    }
    public String getId() {
        return id;
    }
}
