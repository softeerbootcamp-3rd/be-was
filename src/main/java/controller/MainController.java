package controller;

import annotation.Controller;
import annotation.GetMapping;
import annotation.RequestMapping;
import webserver.Model;

@Controller
@RequestMapping("")
public class MainController implements BasicController{
    @GetMapping(url = "/")
    public String index(Model model){
        return "index";
    }

    @GetMapping(url = "/header")
    public String header(Model model){
        return "header";
    }
}
