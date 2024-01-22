package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import model.Response;

public class PageReader {

    public static Response getPage(String url, String filePath) {
        File file = new File(filePath);

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] body = new byte[(int) file.length()];
            fis.read(body);
            return new Response(200, url, body);
        } catch (IOException e) {
            byte[] body = "404 Not Found".getBytes();
            return new Response(404, body);
        }
    }
}
