package HTTPModel;

import model.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Session {

    public static Map<String, User> SessionMap = Collections.synchronizedMap(new HashMap<>());

    public static void addSession(String sid, User user){
        SessionMap.put(sid, user);
    }

    public User getSession(String sid){

        User user = SessionMap.get(sid);
        return user;
    }
}
