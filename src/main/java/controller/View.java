package controller;

import model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class View {
    private static final Logger logger = LoggerFactory.getLogger(View.class);
    private String viewPath;

    public View(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(Request request, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        File file = new File(viewPath);
        byte[] body = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file);) {
            fileInputStream.read(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //todo /user/create가 성공하면 index로 실패하면 form_failed로 가도록 fix 필요
        if (request.getURI().startsWith("/user/create")) {
            response302Header(dos);
        }

        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    public void render(Request request, OutputStream out, String type) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        File file = new File(viewPath);
        byte[] body = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file);) {
            fileInputStream.read(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        response200HeaderContent(dos, body.length, type);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    private void response200HeaderContent(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/" +type+ ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 OK \r\n");
            dos.writeBytes("Location: " + "/index.html" + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
