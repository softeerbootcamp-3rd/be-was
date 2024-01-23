package Functions;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class FileBytes {

    private String fileURL;

    public static byte[] FilesreadAllBytes(String fileURL){
        byte[] body = new byte[0];
        try (FileInputStream fis = new FileInputStream(fileURL);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            return body = bos.toByteArray();
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        }
        return body;
    }


}
