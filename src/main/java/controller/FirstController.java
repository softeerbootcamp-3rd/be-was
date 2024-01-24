package controller;

import db.Database;
import httpmessage.HttpSession;
import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FirstController {
    private final Map<String, Controller> controllerMap = new HashMap<>();
    public FirstController() {
        initControllerMapping();
    }
    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        final String requestPath =  httpRequest.getPath();
        httpResponse.setExtension(requestPath);
        Controller controller = controllerMap.get(requestPath);

        if(controller == null){
            controller = new HomeController();
        }

        controller.service(httpRequest,httpResponse);

        if(requestPath.contains("list")){
            Database database = new Database();
            Collection<User> users = database.findAll();
            String body = new String(httpResponse.getBody());

            StringBuilder stringBuilder = new StringBuilder();
            int index = 1;
            for (User user : users) {
                stringBuilder.append("<tr>\n")
                        .append("<th scope=\"row\">").append(index++).append("</th>")
                        .append("<td>").append(user.getUserId()).append("</td>")
                        .append("<td>").append(user.getName()).append("</td>")
                        .append("<td>").append(user.getEmail()).append("</td>")
                        .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n")
                        .append("</tr>\n");
            }

            String userConent = stringBuilder.toString();
            String modifiedContent = body.replace("{{list}}",userConent);
            // 수정된 문자열을 다시 바이트 배열로 변환
            byte[] modifiedBody = modifiedContent.getBytes();
            httpResponse.setBody(modifiedBody);
        }

        if(httpResponse.getExtension().contains("html"))
        {
            if (!httpRequest.getCookie().isEmpty()) {
                byte[] modifiedBody = getBytes(httpRequest, httpResponse);

                httpResponse.setBody(modifiedBody);
            } else {
                //logger.debug(httpRequest.getCookie());
                String body = new String(httpResponse.getBody());
                String modifiedContent = body.replace("{{login}}",
                        "<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>\n" +
                                "<li><a href=\"user/form.html\" role=\"button\">회원가입</a></li>\n");

                // 수정된 문자열을 다시 바이트 배열로 변환
                byte[] modifiedBody = modifiedContent.getBytes();
                httpResponse.setBody(modifiedBody);
            }
        }

    }

    private byte[] getBytes(HttpRequest httpRequest, HttpResponse httpResponse) {
        HttpSession httpSession = new HttpSession();

        String sid = httpRequest.getCookie();
        String userId = httpSession.getUser(sid).getUserId();

        String body = new String(httpResponse.getBody());
        String modifiedContent = body.replace("{{login}}",
                "<li>" + userId + "</li>\n" +
                        "<li><a href=\"#\" role=\"button\">로그아웃</a></li>\n" +
                        "<li><a href=\"#\" role=\"button\">개인정보수정</a></li>\n");

        // 수정된 문자열을 다시 바이트 배열로 변환
        byte[] modifiedBody = modifiedContent.getBytes();
        return modifiedBody;
    }

    private void initControllerMapping() {
        controllerMap.put("/", new HomeController());
        controllerMap.put("/index.html", new HomeController());
        controllerMap.put("/user/create", new UserCreateController());
        controllerMap.put("/user/login", new UserLoginController());
        controllerMap.put("/user/list", new UserListController());
    }

}
