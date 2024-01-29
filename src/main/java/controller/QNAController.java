package controller;

import annotation.*;
import dto.QnaDto;
import model.Qna;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.QnaService;
import service.UserService;
import webserver.Model;

@Controller
@RequestMapping("/qna")
public class QNAController implements BasicController{
    private static final Logger logger = LoggerFactory.getLogger(QNAController.class);
    private final QnaService qnaService = QnaService.getInstance();

    @GetMapping(url = "/form")
    public String qnaForm(){
        return "qna/form";
    }

    @PostMapping(url = "/form")
    public String qnaCreate(@RequestBody QnaDto qnaDto, Model model){
        Qna qna = new Qna(qnaDto);
        qna.setWriter((User) model.getAttribute("user"));
        qnaService.create(qna);
        return "redirect:/";
    }

}
