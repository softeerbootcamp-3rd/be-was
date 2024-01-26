package controller;

import db.Database;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.Util;

import java.io.*;
import java.io.File;
import java.util.Collection;
import java.util.UUID;

public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    public static Response control(Request request) throws IOException {
        String statusCode = null;
        byte[] body = null;
        String mimeType = request.getMimeType();
        String redirectUrl = null;
        String sessionId = null;
        String base = "./src/main/resources";
        String path = request.getPath();
        String method = request.getMethod();

        if(request.getMethod().equals("GET")) {
            return GetController.controlGet(request);
        }
        else if(method.equals("POST")) {
            return PostController.controlPost(request);
        }
        else if(method.equals("PATCH")) {
            //TODO
        }
        else if(method.equals("PUT")) {
            //TODO
        }
        else if(method.equals("DELETE")) {
            //TODO
        }

        return new Response(statusCode, body, mimeType, redirectUrl, sessionId);
    }
}