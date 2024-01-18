package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import config.ControllerHandler;
import config.HTTPRequest;
import config.HTTPResponse;
import config.ResponseCode;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static webserver.WebServer.users;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.


            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));


            HTTPRequest request = new HTTPRequest(br, logger);

            logger.debug("Ending Connected IP : {}, Port : {}", connection.getInetAddress(),
                    connection.getPort());
            System.out.println("================================================================");
            DataOutputStream dos = new DataOutputStream(out);

            //String templateFilePath = "be-was/src/main/resources/templates";
            //String templateFilePath = "/Users/qkreh/IdeaProjects/be-was/src/main/resources/templates";
            String templateFilePath = "/Users/user/IdeaProjects/be-was/src/main/resources/templates";


            String url = request.getUrl();


            ///////새로 작성중 시작//////////////////////////////
            HTTPResponse response;
            ControllerHandler controllerHandler = null;
            String urlFrontPart = url.split("\\?")[0];
            for (ControllerHandler handler : ControllerHandler.values()) {
                if (handler.url.equals(urlFrontPart)) {
                    controllerHandler = handler;
                    break;
                }
            }

            // 적절한 컨트롤러 핸들러를 찾았을 경우, 해당 핸들러의 메소드 호출
            if (controllerHandler != null) {
                response = controllerHandler.toController(request);
                byte[] head = response.getHead();
                byte[] body = response.getBody();

                dos.write(head, 0, head.length);
                dos.writeBytes("\r\n");
                dos.write(body, 0, body.length);
                dos.flush();
            } else {
                response = new HTTPResponse("HTTP/1.1", ResponseCode.NOT_FOUND.code, ResponseCode.NOT_FOUND.toString());
            }

            //////////새로 작성중 끝////////////////////////////


//
//            // 1. 입력된 url이 /index.html인경우
//            if(url.equals("/index.html")) {
//
//                // 1. File이라는 객체를 templateFilePath + url로 접근
//                // 2. File객체를 Path객체로 변환 후 내용을 body에 저장
//                Path path = new File(templateFilePath + url).toPath();
//                byte[] body = Files.readAllBytes(path);
//                //클라이언트에게 (헤더 + 바디) 응답
//                response200Header(dos, body.length);
//                responseBody(dos, body);
//            }
//            // 2. 입력된 url이 /user인경우
//            else if(url.split("/")[1].equals("user")){
//                // 2-1. /user/form.html 인경우
//                if(url.split("/")[2].equals("form.html")){
//                    Path path = new File(templateFilePath+url).toPath();
//                    byte[] body = Files.readAllBytes(path);
//
//                    response200Header(dos, body.length);
//                    responseBody(dos, body);
//                }
//                // 2-2. /user/create? ... 인경우
//                else if(url.split("/")[2].startsWith("create?")){
//                    String data =  url.split("/")[2].split("\\?")[1];
//                    System.out.println("전달 받은 파라미터 원형: "+data);
//                    //userId=asdf&password=sadf&name=asdf&email=asdf%40asdf
//                    Map<String,String> map = querytoMap(data);
//                    User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
//                    users.add(user);
//                    byte[] body = user.toString().getBytes();
//                    System.out.println(user);
//                    response200Header(dos, body.length);
//                    responseBody(dos, body);
//                }
//            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
            //\r\n 두개는 헤더와 바디를 구분하게 해준다
            logger.debug(dos.toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            //dos
            dos.write(body, 0, body.length);
            //flush를 사용하면 dos에 있는 내용을 전부 보낸다
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Map<String,String> querytoMap(String query){
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
