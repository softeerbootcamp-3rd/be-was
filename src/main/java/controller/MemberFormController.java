package controller;

import dto.UserDto;
import frontController.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.RequestHandler;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

public class MemberFormController implements Controller{
    private final UserService userService = UserService.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        try{
            UserDto dto = new UserDto();

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
