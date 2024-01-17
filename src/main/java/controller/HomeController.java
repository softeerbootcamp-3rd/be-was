package controller;


public class HomeController {
    private static HomeController instance;

    private HomeController() {
    }

    public static HomeController getInstance() {
        if (instance == null) {
            instance = new HomeController();
        }
        return instance;
    }

    public String route(String path) {
        if (path.equals("/")) {
            return "302 /index.html";
        }
        else {
            return "200 " + path;
        }
    }
}
