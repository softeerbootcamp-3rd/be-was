package controller;


public class HomeController {
    public String route(String path) {
        if (path.equals("/")) {
            return "302 /index.html";
        }
        else {
            return "200 " + path;
        }
    }
}
