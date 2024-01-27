package controller;

import db.SessionStorage;
import model.*;
import java.io.*;

public class FirstController {
    public static Response route(Request request) throws IOException {

        // 만료기한이 지난 세션을 삭제 처리
        SessionStorage.updateStorage();

        // 요청에 실린 sessionId가 유효한지 판단해서 로그인 여부 저장
        boolean login = SessionStorage.verifySession(request.getSessionId());

        Response response = new Response(null, null, request.getMimeType(), null, null);

        if(request.getPath().startsWith("/user"))
            UserController.route(request, response, login);
        else
            MainController.route(request, response, login);

        return response;
    }
}