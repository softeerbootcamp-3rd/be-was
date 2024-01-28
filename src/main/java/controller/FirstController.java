package controller;

import db.SessionStorage;
import model.*;
import java.io.*;

public class FirstController {
    public static Response route(Request request) throws IOException {

        // 만료기한이 지난 세션을 삭제 처리
        SessionStorage.updateStorage();

        // 요청에 실린 sessionId가 유효한지 판단해서 로그인 여부 저장
        String verifiedSessionId = SessionStorage.verifySession(request.getSessionId());

        Response response = new Response();

        if(request.getPath().startsWith("/user"))
            UserController.route(request, response, verifiedSessionId);
        else
            MainController.route(request, response, verifiedSessionId);

        return response;
    }
}