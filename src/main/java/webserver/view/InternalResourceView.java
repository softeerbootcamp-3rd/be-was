package webserver.view;

import http.HttpContentType;
import http.HttpStatus;
import http.Request;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Model;

import java.io.*;
import java.util.Base64;

public class InternalResourceView implements View{
    private static final Logger logger = LoggerFactory.getLogger(InternalResourceView.class);
    private String viewPath;
    public InternalResourceView(String viewPath) {
        this.viewPath = viewPath;
    }

    @Override
    public String getContentType() {
        int dotIndex = viewPath.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < viewPath.length() - 1) {
            String fileExtension = viewPath.substring(dotIndex + 1);
            return HttpContentType.getValue(fileExtension);
        }
        return null;
    }

    @Override
    public void render(Request request, Response response, Model model){
        try {
            forward(request, response);
        } catch (IOException e){
            return;
        }
    }

    public String getViewPath() {
        return viewPath;
    }

    private void forward(Request request, Response response) throws IOException {
        File file = new File(viewPath);
        byte[] body;
        if (file.exists() && file.isFile()) {
            String fileContent = Base64.getEncoder().encodeToString(readAllBytes(file));

            body = Base64.getDecoder().decode(fileContent);
            response.setStatus(HttpStatus.OK);
        }
        else{
            body = readAllBytes(new File("/not-found.html"));
            response.setStatus(HttpStatus.NOT_FOUND);
        }
        response.send(body,request);
    }

    public static byte[] readAllBytes(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        }
    }
}
