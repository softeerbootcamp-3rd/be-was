package controller;

import annotation.Controller;
import annotation.GetMapping;
import annotation.RequestMapping;
import dto.UserDto;
import http.SessionManager;
import model.Qna;
import model.User;
import service.QnaService;
import webserver.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("")
public class MainController implements BasicController{
    QnaService qnaService = QnaService.getInstance();
    @GetMapping(url = "/")
    public String index(Model model){
        String posts = qnaService.getPostList();
        model.addAttribute("posts",posts);
        return "index";
    }

    @GetMapping(url = "/header")
    public String header(Model model){
        return "header";
    }
}
