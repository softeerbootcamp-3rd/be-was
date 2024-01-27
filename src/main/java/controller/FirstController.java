package controller;

import model.*;
import java.io.*;

public class FirstController {
    public static Response route(Request request) throws IOException {

        Response response = new Response(null, null, request.getMimeType(), null, null);

        if(request.getPath().startsWith("/user"))
            UserController.route(request, response);
        else
            MainController.route(request, response);

        return response;
    }
}