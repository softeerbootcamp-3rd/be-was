package controller;

import db.SessionStorage;
import model.*;
import java.io.*;

public class FirstController {
    public static Response route(Request request) throws IOException {

        SessionStorage.updateStorage(); // 만료기한이 지난 세션을 삭제 처리
        SessionStorage.verifySession(request.getSessionId()); // 요청에 실린 sessionId가 유효한지 검사

        Response response = new Response(null, null, request.getMimeType(), null, null);

        if(request.getPath().startsWith("/user"))
            UserController.route(request, response);
        else
            MainController.route(request, response);

        return response;
    }
}