package controller;

import model.Request;
import model.Response;

public class MainController {
    public static void route(Request request, Response response){

        String path = request.getPath();

        if (path.equals("/")) {
            response.setStatusCode("302");
            response.setRedirectUrl("/index.html");
            return;
        }

        LastController.route(request, response);
    }
}
