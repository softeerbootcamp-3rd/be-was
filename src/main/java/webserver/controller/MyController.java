package webserver.controller;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyController {
  private final Logger logger = LoggerFactory.getLogger(MyController.class);
  @Controller(uri = "/user/form.html")
  public String joinForm(String word,Integer age){
    logger.info("Controller executed word : {}, age :{}",word,age);
    return "/user/form.html";
  }
  ///create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net
  @Controller(uri = "/user/create")
  public String join(String userId,String password,String email,String name){
    if(Database.findUserById(userId)==null)
      Database.addUser(new User(userId, password, name, email));
    else
      throw new DuplicateUserException();
    return "redirect:/user/form.html";
  }
}
