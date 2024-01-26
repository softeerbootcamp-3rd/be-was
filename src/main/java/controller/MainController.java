package controller;

import annotation.Controller;
import annotation.CookieValue;
import annotation.GetMapping;
import annotation.RequestMapping;
import jdk.jfr.Registered;
import model.User;
import webserver.Model;

@Controller
@RequestMapping("")
public class MainController implements RequestController{
    @GetMapping(url = "/")
    public String index(Model model){
        return "index";
    }
}
