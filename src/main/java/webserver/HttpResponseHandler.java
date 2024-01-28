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
    }

    public static void responseHeader(HttpResponse httpResponse, DataOutputStream dos) throws IOException {
        try {
            dos.writeBytes(httpResponse.getStatusLine().getVersion() + " " +
                    httpResponse.getStatusCodeAndReasonPhrase() + "\r\n");

            if (httpResponse.getStatusCode() == 404) {
                dos.writeBytes("\r\n");
                return;
            }

            if (httpResponse.getStatusCode() == 302) {
                dos.writeBytes("Location: " + httpResponse.getRedirectUri() + "\r\n");
            } else {
                dos.writeBytes("Content-Type: " + httpResponse.getContentType() + ";charset=utf-8\r\n");
                dos.writeBytes("Content-Length: " + httpResponse.getBodyLength() + "\r\n");
            }

            if (httpResponse.getSid() != null) {
                dos.writeBytes("Set-Cookie: sid=" + httpResponse.getSid() + "; Max-Age=120\r\n");
            }

            dos.writeBytes("\r\n");

            if (httpResponse.getStatusCode() != 302) {
                responseBody(dos, httpResponse.getBody());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    private static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
