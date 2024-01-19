package controller;

import http.Request;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.net.URLDecoder;
import java.util.Map;

public class UserController implements Controller{
    private final UserService userService = UserService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Override
    public String process(Request req, Map<String, String> model) {
        logger.debug(new StringBuilder("[MemberFormController.process] req.getUrl() : ").append(req.getUrl()).toString());
        String subPath = req.getUrl().replace("/user/", "");
        switch (subPath) {
            case "create":
                return createUser(req, model);
            // Add more cases for other sub-paths as needed
            default:
                logger.warn("Unknown subPath: {}", subPath);
                return null;
        }

    }

    private String createUser(Request req, Map<String, String> model){
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
