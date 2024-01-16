package service;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import utils.ResponseBuilder;

public class HomeService {

    public void getIndex(String urn, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String filePath = "src/main/resources/templates" + urn;

        byte[] body = Files.readAllBytes(new File(filePath).toPath());
        ResponseBuilder.response200Header(dos, body.length, urn);
        ResponseBuilder.responseBody(dos, body);
    }

    public void getStatic(String urn, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String filePath = "src/main/resources/static" + urn;

        byte[] body = Files.readAllBytes(new File(filePath).toPath());
        ResponseBuilder.response200Header(dos, body.length, urn);
        ResponseBuilder.responseBody(dos, body);
    }
}
