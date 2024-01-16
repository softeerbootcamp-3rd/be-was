package controller;

import frontController.ModelAndView;

import java.util.Map;

public class MemberFormController implements Controller{
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        return "redirect:/login.html";
    }
}
