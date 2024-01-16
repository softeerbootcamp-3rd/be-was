package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyController {
  private final Logger logger = LoggerFactory.getLogger(MyController.class);
  @Controller(uri = "/user/form.html")
  public String join(String word,int age){
    logger.info("Controller executed word : {}, age :{}",word,age);
    return "/index.html";
  }
}
