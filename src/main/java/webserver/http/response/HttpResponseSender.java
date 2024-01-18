package webserver.http.response;

import webserver.http.response.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class HttpResponseSender {
    public void sendResponse(HttpResponse response, DataOutputStream dos) throws IOException {
        sendStatusLine(response, dos);
        sendHeaders(response, dos);
        sendBody(response, dos);
    }

    private void sendStatusLine(HttpResponse response, DataOutputStream dos) throws IOException {
        int statusCode = response.getStatusCode();
        dos.writeBytes(response.getHttpVersion() + " "  + response.getStatusCode() + " " + response.getStatusText() + "\r\n");
    }

    private void sendHeaders(HttpResponse response, DataOutputStream dos) throws IOException {
        for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
            dos.writeBytes(header.getKey() + ": " + header.getValue() + "\r\n");
        }
        dos.writeBytes("\r\n");
    }

    private void sendBody(HttpResponse response, DataOutputStream dos) throws IOException {
        byte[] body = response.getBody();
        if (body != null) {
            dos.write(body, 0, body.length);
            dos.flush();
        }
    }
}
