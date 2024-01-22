package controller;

import annotation.Controller;
import annotation.RequestMapping;
import util.ResourceLoader;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class ResourceController {

    @RequestMapping(method = "GET", path = "/index.html")
    public static HttpResponse index(HttpRequest request) throws IOException {
        HttpResponse response = ResourceLoader.getFileResponse("/index.html");
        String fileContent = new String(response.getBody());
        response.setBody(fileContent.replace("{user-name}", ""));
        return response;
    }
}
