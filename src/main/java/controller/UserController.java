package controller;

import config.*;
import db.Database;
import model.User;

import java.util.UUID;

import static webserver.RequestHandler.threadUuid;


public class UserController {

    public static HTTPResponse createAccount(HTTPRequest request) {
        HTTPResponse response = null;
        try {
            //request에서 구현된 Body의 hashMap을 이용
            String userId = request.getBody().get("userId");
            String password = request.getBody().get("password");
            String name = request.getBody().get("name");
            String email = request.getBody().get("email").replace("%40", "@");

            //유저 아이디가 이미 존재하는 경우 예외 발생
            //그러나 현재 어떻게 처리할지 명시되지 않아서 catch문은 미구현
            if(Database.findUserById(userId)!=null)
                throw new ExceptionType.UserIdTaken("User id already in use");

            //유저 저장
            User user = new User(userId, password, name, email);
            Database.addUser(user);

            // 성공 시 /index.html로 리디렉션
            byte[] body = new byte[0];
            byte[] head = ("HTTP/1.1 " + ResponseCode.REDIRECT.code+ " " + ResponseCode.REDIRECT +"\r\n"+
                    "Location: /index.html" + "\r\n" +
                    "Content-Length: " + body.length  + "\r\n").getBytes();

            response = new HTTPResponse("HTTP/1.1",ResponseCode.REDIRECT.code, ResponseCode.REDIRECT.toString(), head, body);

        }catch(Exception e){ // 실패시
            byte[] body = new byte[0];
            byte[] head = ("HTTP/1.1" + ResponseCode.SERVER_ERROR.code + " " + ResponseCode.SERVER_ERROR +" \r\n"+
                    "Content-Length: " + body.length  + "\r\n").getBytes();
            response = new HTTPResponse("HTTP/1.1",ResponseCode.SERVER_ERROR.code, ResponseCode.SERVER_ERROR.toString(), head, body);
        }

        return response;
    }
    public static HTTPResponse login(HTTPRequest request) {
        //로그인이 되어 있다면 홈으로 리디렉션
        if (threadUuid.get() != null) {
            byte[] body = new byte[0];
            byte[] head = ("HTTP/1.1 " + ResponseCode.REDIRECT.code + " " + ResponseCode.REDIRECT + "\r\n" +
                    "Location: /index.html" + "\r\n" +
                    "Content-Length: " + body.length + "\r\n").getBytes();

            return new HTTPResponse("HTTP/1.1", ResponseCode.REDIRECT.code, ResponseCode.REDIRECT.toString(), head, body);
        }

        //유저 정보를 비교하기 위해 데이터베이스에서 불러온다
        String userId = request.getBody().get("userId");
        String password = request.getBody().get("password");
        User user = Database.findUserById(userId);

        HTTPResponse response = null;
        //로그인 실패하는 경우 리디렉션 (아이디 없거나 비밀번호 불일치)
        if (user == null || !user.getPassword().equals(password)) {
            byte[] body = new byte[0];
            byte[] head = ("HTTP/1.1 " + ResponseCode.REDIRECT.code + " " + ResponseCode.REDIRECT + "\r\n" +
                    "Location: /user/login_failed.html" + "\r\n" +
                    "Content-Length: " + body.length + "\r\n").getBytes();
            response = new HTTPResponse("HTTP/1.1", ResponseCode.REDIRECT.code, ResponseCode.REDIRECT.toString(), head, body);
        }
        // 로그인 성공시 세션 발급, index.html로 리디렉션
        else {
            UUID uuid = Session.createSession(userId);
            byte[] body = new byte[0];
            byte[] head = ("HTTP/1.1 " + ResponseCode.REDIRECT.code + " " + ResponseCode.REDIRECT + "\r\n" +
                    "Location: /index.html" + "\r\n" +
                    "Set-Cookie: sid=" + uuid +"; Path=/"+ "\r\n" +
                    "Content-Length: " + body.length + "\r\n").getBytes();
            response = new HTTPResponse("HTTP/1.1", ResponseCode.REDIRECT.code, ResponseCode.REDIRECT.toString(), head, body);
        }

        return response;

    }

}
