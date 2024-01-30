package controller;

import config.AppConfig;
import constant.HeaderType;
import model.HttpRequest;
import model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.HtmlBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

        httpResponse.setHttpStatus(mv.getHttpStatus());
        String type = extractType(mv);
        httpResponse.addHeader(HeaderType.CONTENT_TYPE, "text/" + type + ";charset=utf-8");

        if (requiredDynamicRendering(mv)) {
            StringBuilder fileBuilder = new StringBuilder(new String(body));

            Map<String, Object> model = mv.getModel();
            for (String from : model.keySet()) {
                Object to = model.get(from);
                HtmlBuilder.dynamicRender(fileBuilder, from, to);
            }

            body = fileBuilder.toString().getBytes();
        }

        httpResponse.setBody(body);
    }

    private static boolean requiredDynamicRendering(ModelView mv) {
        return mv.getModel().size() > 0;
    }

    private String extractType(ModelView mv) { //todo URIParser역할
        String uri = mv.getViewName();
        int start = uri.lastIndexOf(".");
        return uri.substring(start + 1);
    }

}
