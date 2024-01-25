package http.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class StatusLine {
    private final Logger logger = LoggerFactory.getLogger(StatusLine.class);
    private String httpVersion;
    private HttpStatus status;

    public StatusLine(String httpVersion, HttpStatus status) {
        this.httpVersion = httpVersion;
        this.status = status;
    }

    public void writeStatusLine(DataOutputStream dos) {
        try {
            dos.writeBytes(httpVersion + " " + status.getStatusCodeAndMessage() + "\r\n");
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }
}
