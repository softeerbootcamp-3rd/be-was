package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class HTTPResponseSender {
    private static final String INDEX_HTML_PATH = "/index.html";

    public static void sendHttpResponse(DataOutputStream dos, int lengthOfBodyContent, byte[] body){
        HttpResponseUtils.create200OKResponseHeader(dos, lengthOfBodyContent);
        HttpResponseUtils.createResponseBody(dos, body);
        HttpResponseUtils.flushResponse(dos);
    }

    public static void redirectToHomePage(DataOutputStream dos){
        HttpResponseUtils.create302FoundResponseHeader(dos, INDEX_HTML_PATH);
        HttpResponseUtils.flushResponse(dos);
    }


    private static class HttpResponseUtils {
        private static final Logger logger = LoggerFactory.getLogger(HttpResponseUtils.class);

        public static void create200OKResponseHeader(DataOutputStream dos, int lengthOfBodyContent){
            try{
                dos.writeBytes("HTTP/1.1 200 OK \r\n");
                dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
                dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
                dos.writeBytes("\r\n");
            }
            catch (IOException e){
                logger.error("Error logging response: {}", e.getMessage());
            }
        }

        public static void create302FoundResponseHeader(DataOutputStream dos, String redirectLocation){
            try{
                dos.writeBytes("HTTP/1.1 302 Found \r\n");
                dos.writeBytes("Location: " + redirectLocation + "\r\n");
                dos.writeBytes("\r\n");
            }
            catch (IOException e){
                logger.error("Error logging response: {}", e.getMessage());
            }
        }

        public static void flushResponse(DataOutputStream dos){
            try{
                dos.flush();
            }
            catch (IOException e){
                logger.error("Error logging response: {}", e.getMessage());
            }

        }

        public static void createResponseBody(DataOutputStream dos, byte[] body){
            try{
                dos.write(body, 0, body.length);
            }
            catch (IOException e){
                logger.error("Error logging response: {}", e.getMessage());
            }
        }
    }
}
