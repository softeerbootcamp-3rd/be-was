package util;

import model.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseSender {
    public static void send(HttpResponse httpResponse, OutputStream out) {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            dos.writeBytes(httpResponse.extractStartLine());
            dos.writeBytes(httpResponse.extractHeader());

            byte[] body = httpResponse.getBody();
            if (body != null) {
                dos.write(body);
            }

            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
