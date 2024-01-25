package webserver;

import model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static constant.HttpResponseConstant.CRLF;

public class ResponseSender {
    private static final Logger logger = LoggerFactory.getLogger(ResponseSender.class);
    private static final byte[] NO_BODY = "".getBytes();

    private ResponseSender() {
    }

    private static class SingletonHelper {
        private static final ResponseSender INSTANCE = new ResponseSender();
    }

    public static ResponseSender getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public void send(HttpResponse httpResponse, OutputStream out) {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            dos.writeBytes(httpResponse.getStartLine());
            for (Map.Entry<String, String> header : httpResponse.getHeader().entrySet()) {
                dos.writeBytes(header.getKey() + header.getValue());
            }
            dos.writeBytes(CRLF);

            if (httpResponse.getBody() == NO_BODY) {
                return;
            }
            sendBody(httpResponse, dos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendBody(HttpResponse httpResponse, DataOutputStream dos) {
        try {
            byte[] body = httpResponse.getBody();
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
