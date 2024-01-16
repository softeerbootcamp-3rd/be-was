package controller;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import utils.ResponseBuilder;

public class HomeController {

    public void route(String url, OutputStream out) throws IOException {
        if (url.equals("/") || url.equals("/index.html")) {
            getIndex(out);
        } else {
            getStatic(url, out);
        }
    }

    private void getIndex(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String filePath = "src/main/resources/templates/index.html";

        byte[] body = Files.readAllBytes(new File(filePath).toPath());
        ResponseBuilder.response200Header(dos, body.length, filePath);
        ResponseBuilder.responseBody(dos, body);
    }

    private void getStatic(String url, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String filePath = "src/main/resources/static" + url;

        byte[] body = Files.readAllBytes(new File(filePath).toPath());
        ResponseBuilder.response200Header(dos, body.length, filePath);
        ResponseBuilder.responseBody(dos, body);
    }
}
