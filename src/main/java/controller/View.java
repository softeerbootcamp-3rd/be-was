package controller;

import exception.UserNotFoundException;
import model.Request;
import model.Response;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.HtmlBuilder;

import java.io.*;
import java.util.Map;

public class View {
    private static final Logger logger = LoggerFactory.getLogger(View.class);
    private String viewPath;

    public View(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(Request request, Response response, ModelView mv) {
        File file = new File(viewPath);
        byte[] body = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 동적 페이지 변환을 위한 데이터가 존재한다면
        if (mv.getModel().size() > 0) {
            String fileString = new String(body);
            Map<String, Object> model = mv.getModel();
            for (String key : model.keySet()) {
                String renderedHtml = HtmlBuilder.replace(key, model.get(key));
                fileString = fileString.replace(key, renderedHtml);
            }

            body = fileString.getBytes();
        }

        // todo
        if (mv.getViewName().contains("index.html")) {
            String fileString = new String(body);
            User findUser = null;
            try {
                String userId = request.getCookie("sid");
                findUser = new UserService().findUserById(userId);
            } catch (IllegalArgumentException | UserNotFoundException e) {
                fileString = fileString.replace("{{welcome}}", "");
                body = fileString.getBytes();
                response.setBody(body);
                return;
            }
            String renderedHtml = HtmlBuilder.replace("{{welcome}}", findUser.getName());
            fileString = fileString.replace("{{welcome}}", renderedHtml);
            body = fileString.getBytes();
        }

        response.setBody(body);
    }

    public void render(Request request, Response response, ModelView mv, String type) {
        File file = new File(viewPath);
        byte[] body = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.set200Ok();
        response.putToHeaderMap("Content-Type", "text/" + type + ";charset=utf-8");
        response.putToHeaderMap("Content-Length", String.valueOf(body.length));
        if (mv.getViewName().contains("index.html")) {
            String fileString = new String(body);
            User findUser = null;
            try {
                String userId = request.getCookie("sid");
                findUser = new UserService().findUserById(userId);
            } catch (IllegalArgumentException | UserNotFoundException e) {
                fileString = fileString.replace("{{welcome}}", "");
                body = fileString.getBytes();
                response.setBody(body);
                return;
            }
            String renderedHtml = HtmlBuilder.replace("{{welcome}}", findUser.getName());
            fileString = fileString.replace("{{welcome}}", renderedHtml);
            body = fileString.getBytes();
        }
        response.setBody(body);

    }
}
