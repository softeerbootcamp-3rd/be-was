package webserver;

import constant.HttpHeader;
import constant.HttpStatus;
import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private HttpStatus status;
    private final Map<HttpHeader, String> header;
    private byte[] body;

    public HttpResponse() {
        this.status = HttpStatus.OK;
        this.header = new HashMap<>();
    }

    public void setBody(String body) {
        this.body = body.getBytes();
        this.header.put(HttpHeader.CONTENT_LENGTH, Integer.toString(this.body.length));
    }

    public static HttpResponseBuilder builder() {
        return new HttpResponseBuilder();
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public Map<HttpHeader, String> getHeader() {
        return this.header;
    }

    public byte[] getBody() {
        return this.body;
    }

    public static class HttpResponseBuilder {
        private final HttpResponse httpResponse;

        private HttpResponseBuilder() {
            this.httpResponse = new HttpResponse();
        }

        public HttpResponseBuilder status(HttpStatus status) {
            this.httpResponse.status = status;
            return this;
        }

        public HttpResponseBuilder addHeader(HttpHeader header, String value) {
            this.httpResponse.header.put(header, value);
            return this;
        }

        public HttpResponseBuilder body(String body) {
            this.httpResponse.body = body.getBytes();
            addHeader(HttpHeader.CONTENT_LENGTH, Integer.toString(this.httpResponse.body.length));
            return this;
        }

        public HttpResponseBuilder body(byte[] body) {
            this.httpResponse.body = body;
            addHeader(HttpHeader.CONTENT_LENGTH, Integer.toString(body.length));
            return this;
        }

        public HttpResponse build() {
            if (this.httpResponse.status == null) {
                throw new IllegalStateException("Status is required for HttpResponse.");
            }
            return this.httpResponse;
        }
    }

    public void send(OutputStream out, Logger logger) {
        if (this.status == null)
            throw new IllegalStateException("Status is required for HttpResponse.");
        try {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes("HTTP/1.1 " + this.status.getFullMessage() + " \r\n");
            for (Map.Entry<HttpHeader, String> entry : header.entrySet()) {
                dos.writeBytes(entry.getKey().getValue() + ": " + entry.getValue() + "\r\n");
            }
            dos.writeBytes("\r\n");
            dos.write(this.body, 0, this.body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
