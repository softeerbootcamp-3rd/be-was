package controller;

import db.SessionStorage;
import model.Request;
import model.Response;
import model.Session;
import model.StatusCode;

public class MainController {
    public static void route(Request request, Response response, String verifiedSessionId){


        String path = request.getPath();

        if (path.equals("/")) {
            response.setStatusCode(StatusCode.FOUND);
            response.addHeader("Location", "/index.html");
            return;
        }

        LastController.route(request, response, verifiedSessionId);
    }
}
