package controller;

import annotation.RequestMapping;
import db.UserRepository;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.util.Map;
import java.util.UUID;

public class UserController implements Controller{

    private final UserRepository userRepository = UserRepository.getInstance();

    @RequestMapping(value="/user/create", method = "POST")
    public String createUser(HttpRequest request, HttpResponse response) {
        Map<String, String> data = request.getFormData();
        User user = new User(data.get("userId"), data.get("password"), data.get("name"), data.get("email"));
        userRepository.addUser(user);

        return "redirect:/index";
    }

    @RequestMapping(value="/user/login", method = "POST")
    public String login(HttpRequest request, HttpResponse response) {
        Map<String, String> data = request.getFormData();
        User user = userRepository.findUserById(data.get("userId"));

        if (data.get("password").equals(user.getPassword())) {
            String sid = generateSessionId();
            response.setHeader("Set-Cookie","sid="+sid+"; Max-Age=300; Path=/");
            SessionManager.addSession(sid, user.getUserId());
            return "redirect:/index";
        } else {
            return "redirect:/user/login_failed";
        }
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}