package util;

import model.Response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseSender {
    public static void send(Response response, OutputStream out) {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            dos.writeBytes(response.extractLine());
            dos.writeBytes(response.extractHeader());

            byte[] body = response.getBody();
            if (body != null) {
                dos.write(body);
            }

            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
