package webserver.controller;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MyController {
  private final Logger logger = LoggerFactory.getLogger(MyController.class);
  @GetMapping(uri = "/user/form.html")
  public String joinForm(String word,Integer age){
    logger.info("Controller executed word : {}, age :{}",word,age);
    return "/templates/user/form.html";
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

}
