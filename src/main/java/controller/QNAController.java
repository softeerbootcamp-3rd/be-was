package controller;

import annotation.*;
import model.Qna;

@Controller
@RequestMapping("/qna")
public class QNAController implements BasicController{
    @GetMapping(url = "/form")
    public String qnaForm(){
        return "qna/form";
    }

    @PostMapping(url = "/form")
    public String qnaCreate(@RequestBody QnaDto qnaDto){
        Qna qna = new Qna();
    }

}
