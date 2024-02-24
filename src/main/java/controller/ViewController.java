package controller;

import annotation.Controller;
import annotation.RequestMapping;
import util.web.ResourceLoader;
import model.SharedData;
import webserver.HttpResponse;

import java.io.IOException;

@Controller
public class ViewController {

    @RequestMapping(method = "GET", path = "/user/list")
    public static HttpResponse userList() throws IOException {
        if (SharedData.requestUser.get() == null)
            return HttpResponse.redirect("/index.html");

        byte[] fileContent = ResourceLoader.getFileContent("/user/list.html");
        return HttpResponse.redirect("/user/list.html");
    }
}
