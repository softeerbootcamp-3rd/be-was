package controller;

import annotation.*;
import com.sun.jdi.InternalException;
import dto.UserDto;
import http.Request;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController implements RequestController {
    private final UserService userService = UserService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(){
    }

    @GetMapping(url = "/create")
    public String createUser(@RequestParam(name = "email") String email, @RequestParam(name = "userId") String userId,
                             @RequestParam(name = "name") String name, @RequestParam(name = "password") String password){
        try{
            User user = new User();
            user.setEmail(email);
            user.setUserId(userId);
            user.setName(name);
            user.setPassword(password);
            userService.signUp(user);
        }
        catch (Exception e) {
            throw new InternalException();
        }
        return "redirect:/user/login.html";
    }

    @PostMapping(url = "/create")
    public String createUserPost(@RequestBody UserDto userDto){
        User user = new User(userDto);
        userService.signUp(user);
        return "redirect:/index.html";
    }
}
