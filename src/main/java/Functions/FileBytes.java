package Functions;

import java.io.*;

public class FileBytes {

    private String fileURL;

    public static byte[] FilesreadAllBytes(String fileURL){

        StringBuilder content = new StringBuilder();

        byte[] body = new byte[0];

        try (BufferedReader reader = new BufferedReader(new FileReader(fileURL))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
                System.out.println("line:  " + line);
            }
            return content.toString().getBytes("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        byte[] body = new byte[0];

        try (FileInputStream fis = new FileInputStream(fileURL);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {

                bos.write(buffer, 0, bytesRead);
                //System.out.println("line:" + new String(buffer, 0, bytesRead, "UTF-8"));
            }

            return body = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

         */



        return body;
    }


}
