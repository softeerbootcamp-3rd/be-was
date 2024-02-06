package controller;

import annotations.GetMapping;
import annotations.PostMapping;
import controller.util.FileUtil;
import webserver.http.SessionManager;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import model.User;
import webserver.http.HttpStatus;
import service.UserService;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

public class UserController {
    public static UserController getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final UserController INSTANCE = new UserController();
    }

    private final UserService userService = UserService.getInstance();

    @PostMapping(route = "/user/create")
    public void signup(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, String> userProperties = request.getBody().convertRawStringAsMap();
        String userId = userProperties.getOrDefault("userId", "");
        String password = userProperties.getOrDefault("password", "");
        String name = userProperties.getOrDefault("name", "");
        String email = userProperties.getOrDefault("email", "");

        if (Stream.of(userId, password, name, email)
                .anyMatch(property -> property.isBlank())
        ) {
            response.setStatusCode(HttpStatus.FOUND);
            response.addHeaderProperty("Location", "/user/form_failed.html");
            return;
        }

        boolean isSaved = userService.saveUser(userId, password, name, email);

        if (isSaved) {
            response.setStatusCode(HttpStatus.FOUND);
            response.addHeaderProperty("Location", "/index.html");
        } else {
            response.setStatusCode(HttpStatus.FOUND);
            response.addHeaderProperty("Location", "/user/form_failed.html");
        }
    }

    @PostMapping(route = "/user/login")
    public void login(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, String> userProperties = request.getBody().convertRawStringAsMap();
        String userId = userProperties.getOrDefault("userId", "");
        String password = userProperties.getOrDefault("password", "");

        User user = userService.getUser(userId, password);

        if (user == null) {
            response.setStatusCode(HttpStatus.FOUND);
            response.addHeaderProperty("Location", "/user/login_failed.html");
            return;
        }

        String sid = SessionManager.createSession(user);

        // 5분간 유지되는 세션을 생성하고 쿠키도 5분 후에 만료되도록 한다.
        response.setStatusCode(HttpStatus.FOUND);
        response.addHeaderProperty("Location", "/index.html");
        response.addHeaderProperty("Set-Cookie", "sid=" + sid + "; Path=/; Max-Age=300");
    }

    @GetMapping(route = "/user/list")
    public void list(HttpRequest request, HttpResponse response) throws IOException {
        User sessionUser = SessionManager.findUserByRequest(request);

        if (sessionUser == null) {
            response.setStatusCode(HttpStatus.FOUND);
            response.addHeaderProperty("Location", "/user/login.html");
            return;
        }

        StringBuilder htmlBuilder = new StringBuilder();

        byte[] listTemplate = FileUtil.readFile(new File("src/main/resources/templates/user/list.html"));

        String template = new String(listTemplate);

        String beforeTable = template.substring(0, template.indexOf("<tbody>"));
        String afterTable = template.substring(template.indexOf("</tbody>")+"</tbody>".length());

        htmlBuilder.append(beforeTable).append("<tbody>");

        int count = 1;
        for (User user : userService.listAll()) {
            htmlBuilder.append("<tr>\n")
                    .append("<th scope=\"row\">").append(count++).append("</th> ")
                    .append("<td>").append(user.getUserId()).append("</td> ")
                    .append("<td>").append(user.getName()).append("</td> ")
                    .append("<td>").append(user.getEmail()).append("</td> ")
                    .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n")
                    .append("</tr>");
        }
        htmlBuilder.append("</tbody>").append(afterTable);

        response.setBody(htmlBuilder.toString().getBytes());

        FileUtil.generateDynamicMenuBar(request, response);
    }
}
