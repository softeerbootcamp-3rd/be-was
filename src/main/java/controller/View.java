package controller;

import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class View {
    private static final Logger logger = LoggerFactory.getLogger(View.class);
    private String viewPath;

    public View(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(Request request, Response response, OutputStream out) {
        DataOutputStream dos = new DataOutputStream(out);

        File file = new File(viewPath);
        byte[] body = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.setBody(body);
        flushResponse(dos, response);
    }

    private void flushResponse(DataOutputStream dos, Response response) {
        try {
            logger.debug("response = {}", response.toString());

            dos.writeBytes(response.extractLine());
            dos.writeBytes(response.extractHeader());

            byte[] body = response.getBody();
            if (body != null) {
                dos.write(body);
            }

            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void render(Request request, Response response, OutputStream out, String type) {
        DataOutputStream dos = new DataOutputStream(out);

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
        response.setBody(body);

        flushResponse(dos, response);
    }
}
