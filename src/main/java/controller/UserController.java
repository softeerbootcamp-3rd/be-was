package controller;

import annotation.Controller;
import annotation.GetMapping;
import annotation.RequestMapping;
import http.Request;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController implements BasicController {
    private final UserService userService = UserService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private Map<String, Method> getMap = new HashMap<>();
    private Map<String, Method> postMap = new HashMap<>();

    public UserController(){
    }

    @GetMapping(url = "/create")
    public String createUser(Request req, Map<String, String> model){
        logger.debug("createUser started");
        try{
            User user = new User();
            Map<String, String> paramMap = req.getRequestParam();
            user.setEmail(URLDecoder.decode(paramMap.get("email"), "UTF-8"));
            user.setUserId(URLDecoder.decode(paramMap.get("userId"), "UTF-8"));
            user.setName(URLDecoder.decode(paramMap.get("name"), "UTF-8"));
            user.setPassword(URLDecoder.decode(paramMap.get("password"), "UTF-8"));
            userService.signUp(user);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return "/user/form.html";
        }
        return "redirect:/user/login.html";
    }
}
