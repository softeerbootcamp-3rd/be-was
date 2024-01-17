package response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class HttpResponse {

//    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private String version;
    private String statusCode;
    private String statusMessage;
    private Map<String, String> headers;
    private String body;



//    public void setVersion(String version) {
//        this.version = version;
//    }
//
//    public void setStatusCode(String statusCode) {
//        this.statusCode = statusCode;
//    }
//
//    public void setStatusMessage(String statusMessage) {
//        this.statusMessage = statusMessage;
//    }
//
//    public void setBody(byte[] body) {
//        this.body = body;
//    }
//
//    byte[] body;
//
//
//    public void send() throws IOException {
//        DataOutputStream dos = new DataOutputStream(out);
//        response200Header(dos, body.length);
//        responseBody(dos, body);
//    }
//
//    public void setRequestUrl(String requestUrl) {
//        this.requestUrl = requestUrl;
//    }
//
//    public Map<String, String> getHeaders() {
//        return headers;
//    }
//
//
//    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
//        try {
//            dos.writeBytes("HTTP/1.1 200 OK \r\n");
//            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
//            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
//            dos.writeBytes("\r\n");
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//    private void responseBody(DataOutputStream dos, byte[] body) {
//        try {
//            dos.write(body, 0, body.length);
//            dos.flush();
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }
}
