package controller;

import model.Request;
import model.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LastController {

    public static void route(Request request, Response response, boolean login) {
        String path = request.getPath();

        if(request.getMimeType().equals("text/html"))
            path = "./src/main/resources/templates" + path;
        else
            path = "./src/main/resources/static" + path;
        File searchedFile = new File(path);


        if(searchedFile.exists()) {
            response.setStatusCode("200");
            File file = new File(path);
            byte[] data = new byte[(int) file.length()];

            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                int bytesRead = fileInputStream.read(data);
                if (bytesRead != file.length()) {
                    throw new IOException("Could not read the entire file");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.setBody(data);
        }
        else {
            response.setStatusCode("302");
            response.setRedirectUrl("/error404.html");
        }
    }
}
