package webserver.controller;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.session.CustomSession;

import java.util.Map;
import java.util.UUID;

public class MyController {
  private final Logger logger = LoggerFactory.getLogger(MyController.class);
  @GetMapping(uri = "/user/form.html")
  public String joinForm(String word,Integer age){
    logger.info("Controller executed word : {}, age :{}",word,age);
    return "/templates/user/form.html";
  }
  @GetMapping(uri = "/user/login.html")
  public String loginForm(){
    return "/templates/user/login.html";
  }
  @GetMapping(uri = "/index.html")
  public String mainPage(){
    Map.Entry<String,String> e;
    return "/templates/index.html";
  }
  @PostMapping(uri = "/user/create")
  public String join(@RequestBody UserForm userForm){
    if(Database.findUserById(userForm.getUserId())==null)
      Database.addUser(new User(userForm.getUserId(), userForm.getPassword(), userForm.getName(), userForm.getEmail()));
    else
      throw new DuplicateUserException();
    return "redirect:/index.html";
  }

  @PostMapping(uri = "/user/login")
  public String login(@RequestBody LoginForm loginForm){
    User findUser = Database.findUserById(loginForm.userId);
    if(findUser==null)
      throw new RuntimeException();
    if(loginForm.correctUser(findUser)) {
      UUID sessionKey = CustomSession.registerUser(findUser.getUserId());
      CustomSession.setCookie.set(sessionKey);
      return "redirect:/index.html";
    }
    return "redirect:/user/login_failed.html";
  }
  @GetMapping(uri = "/user/login_failed.html")
  public String failedLogin(){
    return "/templates/user/login_failed.html";
  }
  public static class LoginForm{
    private String userId;
    private String password;
    public LoginForm(){
    }
    public boolean correctUser(User user){
      return this.userId.equals(user.getUserId()) && this.password.equals(user.getPassword());
    }
  }
}
