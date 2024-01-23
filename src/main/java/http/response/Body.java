package http.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class Body {
    private final Logger logger = LoggerFactory.getLogger(Body.class);
    byte[] body;

    public Body(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public void writeBody(DataOutputStream dos) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    public String getBodyLength() {
        return String.valueOf(body.length);
    }
}
