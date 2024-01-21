package controller;

import config.HTTPRequest;
import config.HTTPResponse;
import config.ResponseCode;
import db.Database;
import model.User;

import java.util.HashMap;
import java.util.Map;


public class UserController {

    public static HTTPResponse createAccount(HTTPRequest request) {
        HTTPResponse response = null;
        try {
            String url = request.getUrl();
            String data = url.split("/")[2].split("\\?")[1];
            System.out.println("전달 받은 파라미터 원형: " + data);
            //userId=asdf&password=sadf&name=asdf&email=asdf%40asdf
            Map<String, String> map = querytoMap(data);
            User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
            Database.addUser(user);
            byte[] body = user.toString().getBytes();
            byte[] head = ("HTTP/1.1" + ResponseCode.OK.code + ResponseCode.OK +" \r\n"+
                    "Content-Type: text/html;charset=utf-8\r\n"+
                    "Content-Length: " + body.length  + "\r\n").getBytes();
            response = new HTTPResponse("HTTP/1.1",ResponseCode.OK.code, ResponseCode.OK.toString(), head, body);

        }
        catch(Exception e){
            byte[] body = new byte[0];
            byte[] head = ("HTTP/1.1" + ResponseCode.SERVER_ERROR.code + ResponseCode.SERVER_ERROR +" \r\n"+
                    "Content-Type: text/html;charset=utf-8\r\n"+
                    "Content-Length: " + body.length  + "\r\n").getBytes();
            response = new HTTPResponse("HTTP/1.1",ResponseCode.SERVER_ERROR.code, ResponseCode.SERVER_ERROR.toString(), head, body);
        }

        return response;
    }
    private static Map<String,String> querytoMap(String query){
        Map<String, String> map = new HashMap<>();

        //&를 기준으로 한쌍으로 묶는다.
        String[] pairs = query.split("&");
        for(String pair:pairs){

            //url로 온 경우 @를 %40으로 인코딩 돼서 온다.
            // @는 url에서 의미를 갖기 때문. 따라서 다시 @로 변환해 주어야한다
            pair = pair.replace("%40","@");

            //=를 기준으로 분리한다
            String[] values = pair.split("=");

            //value값을 빈값으로 받았을 때는 널값으로 처리
            if(values.length == 1)
                map.put(values[0],null);
            else if(values.length == 2)
                map.put(values[0], values[1]);
        }
        return map;
    }
}
