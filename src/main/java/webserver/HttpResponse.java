package webserver;

import constant.HttpHeader;
import constant.HttpStatus;
import constant.MimeType;
import org.slf4j.Logger;
import util.html.HtmlBuilder;
import util.web.ResourceLoader;

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
        this.body = new byte[0];
    }

    public void setBody(String body) {
        this.body = body.getBytes();
        this.header.put(HttpHeader.CONTENT_LENGTH, "" + this.body.length);
    }

    public static HttpResponseBuilder builder() {
        return new HttpResponseBuilder();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<HttpHeader, String> getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
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
            addHeader(HttpHeader.CONTENT_LENGTH, "" + this.httpResponse.body.length);
            return this;
        }

        public HttpResponseBuilder body(byte[] body) {
            this.httpResponse.body = body;
            addHeader(HttpHeader.CONTENT_LENGTH, "" + body.length);
            return this;
        }

        public HttpResponse build() {
            if (this.httpResponse.status == null) {
                throw new IllegalStateException("Status is required for HttpResponse.");
            }
            return httpResponse;
        }
    }

    public static HttpResponse forward(String path) throws IOException {
        byte[] fileContent = ResourceLoader.getFileContent(path);

        // html 파일이면 동적으로 내용 변경
        if (MimeType.HTML.getMimeType().equals(ResourceLoader.getMimeType(path)))
            fileContent = HtmlBuilder.process(fileContent);

        return builder()
                .status(HttpStatus.OK)
                .addHeader(HttpHeader.CONTENT_TYPE, ResourceLoader.getMimeType(path))
                .body(fileContent)
                .build();
    }

    public static HttpResponse redirect(String path) {
        return builder().status(HttpStatus.FOUND)
                .addHeader(HttpHeader.LOCATION, path)
                .build();
    }

    public static HttpResponse of(HttpStatus status) {
        return redirect("/error/" + status.getCode() + ".html");
    }

    public void send(OutputStream out, Logger logger) {
        if (status == null)
            throw new IllegalStateException("Status is required for HttpResponse.");
        try {
            out.write(("HTTP/1.1 " + status.getFullMessage() + " \r\n").getBytes());
            for (Map.Entry<HttpHeader, String> entry : header.entrySet()) {
                out.write((entry.getKey().getValue() + ": " + entry.getValue() + "\r\n").getBytes());
            }
            out.write("\r\n".getBytes());
            out.write(body);
            out.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
