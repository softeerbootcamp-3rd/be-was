package http.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Headers {
    private final Logger logger = LoggerFactory.getLogger(Headers.class);
    private Map<String, String> headers;

    public Headers() {
        this.headers = new HashMap<>();
    }

    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    void writeHeader(DataOutputStream dos) {
        try {
            for (String key : headers.keySet()) {
                dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }
}
