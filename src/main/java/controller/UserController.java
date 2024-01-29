package controller;

import annotation.RequestMapping;
import auth.SessionManager;
import controller.dto.InputData;
import controller.dto.OutputData;
import db.UserRepository;
import model.User;
import view.View;

import java.util.UUID;

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

        View view = outputData.getView();
        view.set("users", UserRepository.findAll());
        return "/user/list";
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}