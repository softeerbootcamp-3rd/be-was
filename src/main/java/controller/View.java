package controller;

import config.AppConfig;
import exception.UserNotFoundException;
import model.Qna;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.HtmlBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class View {
    private static final Logger logger = LoggerFactory.getLogger(View.class);
    private final UserService userService = AppConfig.userService();
    private String viewPath;

    public View(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(HttpRequest httpRequest, HttpResponse httpResponse, ModelView mv) {
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

            Object attribute = mv.getAttribute("{{qna-list}}");
            if (attribute != null) {
                Collection<Qna> qnaCollection = (Collection<Qna>) attribute;
                ArrayList<Qna> qnas = new ArrayList<>(qnaCollection);
                if (qnas.size() == 0) {
                    fileString = fileString.replace("{{qna-list}}", "");
                } else {
                    String rendered = HtmlBuilder.replace("{{qna-list}}", qnas);
                    fileString = fileString.replace("{{qna-list}}", rendered);
                }
            }

            User findUser = null;
            try {
                String userId = httpRequest.getCookie("sid");
                findUser = userService.findUserById(userId);
            } catch (IllegalArgumentException | UserNotFoundException e) {
                fileString = fileString.replace("{{welcome}}", "");
                body = fileString.getBytes();
                httpResponse.setBody(body);
                return;
            }
            String renderedHtml = HtmlBuilder.replace("{{welcome}}", findUser.getName());
            fileString = fileString.replace("{{welcome}}", renderedHtml);
            body = fileString.getBytes();
        }

        httpResponse.setBody(body);
    }

    public void render(HttpRequest httpRequest, HttpResponse httpResponse, ModelView mv, String type) {
        File file = new File(viewPath);
        byte[] body = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        httpResponse.set200Ok();
        httpResponse.addHeader("Content-Type", "text/" + type + ";charset=utf-8");

//        // todo
        if (mv.getViewName().contains("index.html")) {
            String fileString = new String(body);

            Collection<Qna> attribute = (Collection<Qna>) mv.getAttribute("{{qna-list}}");
            ArrayList<Qna> qnas = new ArrayList<>(attribute);
            if (qnas.size() == 0) {
                fileString = fileString.replace("{{qna-list}}", "");
            }
            String rendered = HtmlBuilder.replace("{{qna-list}}", qnas);
            fileString = fileString.replace("{{qna-list}}", rendered);

            User findUser = null;
            try {
                String userId = httpRequest.getCookie("sid");
                findUser = userService.findUserById(userId);
            } catch (IllegalArgumentException | UserNotFoundException e) {
                fileString = fileString.replace("{{welcome}}", "");
                body = fileString.getBytes();
                httpResponse.setBody(body);
                return;
            }
            String renderedHtml = HtmlBuilder.replace("{{welcome}}", findUser.getName());
            fileString = fileString.replace("{{welcome}}", renderedHtml);
            body = fileString.getBytes();
        }
        httpResponse.addHeader("Content-Length", String.valueOf(body.length));
        httpResponse.setBody(body);

    }
}
