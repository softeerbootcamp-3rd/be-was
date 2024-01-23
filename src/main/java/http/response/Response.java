package http.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Response {
    private final Logger logger = LoggerFactory.getLogger(Response.class);
    private StatusLine statusLine;
    private Headers headers;
    private Body body;
    private DataOutputStream dos;

    public Response(DataOutputStream dos) {
        this.dos = dos;
        headers = new Headers();
    }

    public byte[] getBody() {
        return body.getBody();
    }

    public void addHeader(String header, String value) {
        headers.addHeader(header, value);
    }

    public void ok(String filePath) {
        addStatusLine("HTTP/1.1", HttpStatus.OK);
        addResponseBody(filePath);
        addHeader("Content-Length", body.getBodyLength());
        writeStatusAndHeader();
        body.writeBody(dos);

        logger.debug("filePath : {}", filePath);
    }

    public void redirect(String redirectPath) {
        addStatusLine("HTTP/1.1", HttpStatus.REDIRECT);
        addHeader("Location", redirectPath);
        writeStatusAndHeader();

        logger.debug("redirectPath : {}", redirectPath);
    }

    private void addStatusLine(String httpVersion, HttpStatus status) {
        statusLine = new StatusLine(httpVersion, status);
    }

    private void addResponseBody(String url) {
        try {
            File file = new File(url);
            if (!file.exists()) {
                this.body = new Body("404 File Not Found".getBytes());
            }
            if (file.exists()) {
                this.body = new Body(Files.readAllBytes(file.toPath()));
            }
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }

    private void writeStatusAndHeader() {
        statusLine.writeStatusLine(dos);
        headers.writeHeader(dos);
    }

}
