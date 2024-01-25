package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import response.http.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class ViewResolver {

    private static final Logger logger = LoggerFactory.getLogger(ViewResolver.class);

    public static void response(HttpResponse httpResponse, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        responseHeader(httpResponse, dos);
        responseBody(dos, httpResponse.getBody());
    }

    public static void responseHeader(HttpResponse httpResponse, DataOutputStream dos) throws IOException {
        try {
            dos.writeBytes(httpResponse.getStatusLine().getVersion() + " " +
                            httpResponse.getStatusCodeAndReasonPhrase() + "\r\n");
            if (httpResponse.getStatusCode() == 404) { // 404 Not Found 이면
                dos.writeBytes("\r\n");
                return;
            }
            if (httpResponse.getRedirectUri() != null) { // Redirect Uri가 존재하면
                dos.writeBytes("Location: " + httpResponse.getRedirectUri() + "\r\n");
            }
            if (httpResponse.getSid() != null) { // sessionId가 존재하면
                dos.writeBytes("Set-Cookie: sid=" + httpResponse.getSid() + "; Path=/");
            }
            dos.writeBytes("Content-Type: " + httpResponse.getContentType() + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + httpResponse.getBodyLength() + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        responseBody(dos, httpResponse.getBody());
    }

    private static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush(); //
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
