package controller;

import annotation.*;
import dto.LoginDto;
import dto.UserDto;
import http.Request;
import http.Response;
import http.SessionManager;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.Model;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
@Controller
@RequestMapping("/user")
public class UserController implements BasicController{
    private final UserService userService = UserService.getInstance();
    private final SessionManager sessionManager = SessionManager.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(){
    }

    @GetMapping(url = "/create")
    public String createUser(){
        return "user/form";
    }

    @PostMapping(url = "/create")
    public String createUserPost(@RequestBody UserDto userDto){
        User user = new User(userDto);
        userService.signUp(user);
        return "redirect:/";
    }

    @GetMapping(url = "/login")
    public String loginForm(){
        return "user/login";
    }
    @PostMapping(url = "/login")
    public String login(@RequestBody LoginDto form, Response response) {
        Optional<User> loginMember = userService.login(form.getUserId(), form.getPassword());
        if (loginMember.isEmpty()) {
            return "user/login_failed";
        }

        sessionManager.createSession(loginMember.get(), response, "SID");
        return "redirect:/";

    }

    @GetMapping(url = "/logout")
    public String logout(Request req){
        sessionManager.expire(req,"SID");

        return "redirect:/";
    }

    @GetMapping(url = "/list")
    public String userList(Model model){
        List<User> users = SessionManager.getLoginedUsers();
        List<String> userjson = new ArrayList<>();
        for(User user : users){
            UserDto userDto = new UserDto(user);
            userjson.add(userDto.toJsonString());
        }
        model.addAttribute("users",userjson);
        return "user/list";
    }

    @GetMapping(url = "/profile")
    public String userProfile(@RequestParam(name = "postId") String postId, Model model){
        if(postId!=null){
            String author = userService.userInfo(Long.parseLong(postId));
            model.addAttribute("author",author);
        }
        return "user/profile";

    }
}
