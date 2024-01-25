package http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseSender {

    public static void send(HttpResponse httpResponse, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        responseHeader(dos, httpResponse);
		responseBody(dos, httpResponse);
		dos.flush();
    }
    private static void responseHeader(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        dos.writeBytes(httpResponse.getResponseHeader());
    }

    private static void responseBody(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        byte[] body = httpResponse.getBody();
		dos.write(body, 0, body.length);
    }
}
