package controller;

import annotation.RequestMapping;
import auth.SessionManager;
import controller.dto.ListMapData;
import controller.dto.InputData;
import controller.dto.OutputData;
import db.UserRepository;
import model.User;

import java.util.*;

public class UserController implements Controller{

    private final UserRepository userRepository = UserRepository.getInstance();

    @RequestMapping(value="/user/create", method = "POST")
    public String createUser(InputData data, OutputData outputData) {
        User user = new User(data.get("userId"), data.get("password"), data.get("name"), data.get("email"));
        userRepository.addUser(user);

        return "redirect:/index";
    }

    @RequestMapping(value="/user/login", method = "POST")
    public String login(InputData data, OutputData outputData) {
        User user = userRepository.findUserById(data.get("userId"));

        if (user!=null && data.get("password").equals(user.getPassword())) {
            String sid = generateSessionId();
            outputData.setHeader("Set-Cookie","sid="+sid+"; Max-Age=300; Path=/");
            SessionManager.addSession(sid, user.getUserId());
            return "redirect:/index";
        } else {
            return "redirect:/user/login_failed";
        }
    }

    @RequestMapping(value = "/user/logout", method = "GET")
    public String logout(InputData data, OutputData outputData) {
        String userId = SessionManager.getUserBySessionId(data.getSessionId());
        SessionManager.deleteSession(userId);
        outputData.setHeader("Set-Cookie","sid="+data.getSessionId()+"; Max-Age=0; Path=/");

        return "redirect:/index";
    }

    @RequestMapping(value = "/user/list", method = "GET")
    public String getUserList(InputData data, OutputData outputData) {
        String sessionId = data.getSessionId();
        if (sessionId == null)
            return "redirect:/user/login";

        ListMapData users = new ListMapData();
        for (User user : UserRepository.findAll()) {
            Map<String, String> u = new HashMap<>();
            u.put("userId", user.getUserId());
            u.put("name", user.getName());
            u.put("email", user.getEmail());
            users.putMap(u);
        }
        outputData.getView().set("users", users);
        return "/user/list";
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}