package controller;

import dto.UserDto;
import http.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.FrontController;
import webserver.ModelAndView;

import java.net.URLDecoder;
import java.util.Map;

public class MemberFormController implements Controller{
    private final UserService userService = UserService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);

    @Override
    public String process(Request req, Map<String, Object> model) {
        logger.debug("[MemberFormController.process] req.getUrl() : "+req.getUrl());
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

    private String createUser(Request req, Map<String, Object> model){
        try{
            UserDto dto = new UserDto();
            Map<String, String> paramMap = req.getRequestParam();
            dto.setEmail(URLDecoder.decode(paramMap.get("email"), "UTF-8"));
            dto.setUserId(URLDecoder.decode(paramMap.get("userId"), "UTF-8"));
            dto.setName(URLDecoder.decode(paramMap.get("name"), "UTF-8"));
            dto.setPassword(URLDecoder.decode(paramMap.get("password"), "UTF-8"));
            userService.signUp(dto);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return "/user/form.html";
        }
        return "redirect:/user/login.html";
    }
}
