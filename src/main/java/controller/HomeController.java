package controller;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import utils.ResponseBuilder;

public class HomeController {


    public void route(String urn, OutputStream out) throws IOException {
        if (urn.equals("/") || urn.equals("/index.html")) {
            getIndex(urn, out);
        } else {
            getStatic(urn, out);
        }
    }

    private void getIndex(String urn, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String filePath = "src/main/resources/templates" + urn;

        byte[] body = Files.readAllBytes(new File(filePath).toPath());
        ResponseBuilder.response200Header(dos, body.length, urn);
        ResponseBuilder.responseBody(dos, body);
    }

    private void getStatic(String urn, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String filePath = "src/main/resources/static" + urn;

        byte[] body = Files.readAllBytes(new File(filePath).toPath());
        ResponseBuilder.response200Header(dos, body.length, urn);
        ResponseBuilder.responseBody(dos, body);
    }
}
