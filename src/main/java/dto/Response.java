package dto;

import webserver.HttpStatus;

public class Response {

    private HttpStatus httpStatus;
    private String contentType;
    private byte[] body;

    public Response(HttpStatus httpStatus, String contentType, String str) {
        this.httpStatus = httpStatus;
        this.contentType = contentType;
        this.body = str.getBytes();
    }

    public Response(HttpStatus httpStatus, String contentType, byte[] body) {
        this.httpStatus = httpStatus;
        this.contentType = contentType;
        this.body = body;
    }

    public Response(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.contentType = null;
        this.body = null;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getBody() {
        return body;
    }

    static public class Builder {
        private HttpStatus httpStatus;
        private String contentType;
        private byte[] body;

        public Builder() {

        }

        public Builder(Response response) {
            this.httpStatus = response.httpStatus;
            this.contentType = response.contentType;
            this.body = response.body;
        }

        public Builder httpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public Builder body(String str) {
            this.body = str.getBytes();
            return this;
        }

        public Response build() {
            return new Response(httpStatus, contentType, body);
        }

    }


}
