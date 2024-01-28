package controller;

import db.SessionStorage;
import model.Request;
import model.Response;
import model.Session;

public class MainController {
    public static void route(Request request, Response response, boolean login){


        String path = request.getPath();

        if (path.equals("/")) {
            response.setStatusCode("302");
            response.setRedirectUrl("/index.html");
            return;
        }

        LastController.route(request, response, login);
    }
}
