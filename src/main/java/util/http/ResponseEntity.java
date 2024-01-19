package util.http;

import util.Assert;
import util.MultiValueMap;

import java.net.URI;
import java.util.Optional;
import java.util.function.Consumer;

public class ResponseEntity<T> extends HttpEntity<T> {

    private final HttpStatusCode status;

    public ResponseEntity(HttpStatusCode status) {
        this(null, null, status);
    }

    public ResponseEntity(T body, HttpStatusCode status) {
        this(body, null, status);
    }

    public ResponseEntity(MultiValueMap<String, String> headers, HttpStatusCode status) {
        this(null, headers, status);
    }

    public ResponseEntity(T body, MultiValueMap<String, String> headers, int rawStatus) {
        this(body, headers, HttpStatusCode.valueOf(rawStatus));
    }

    public ResponseEntity(T body, MultiValueMap<String, String> headers, HttpStatusCode statusCode) {
        super(body, headers);
        Assert.notNull(statusCode, "HttpStatusCode must not be null");

        this.status = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return this.status;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<");
        builder.append(this.status);
        if (this.status instanceof HttpStatus) {
            HttpStatus httpStatus = (HttpStatus)this.status;
            builder.append(' ');
            builder.append(httpStatus.getReasonPhrase());
        }
        builder.append(',');
        T body = getBody();
        HttpHeaders headers = getHeaders();
        if (body != null) {
            builder.append(body);
            builder.append(',');
        }
        builder.append(headers);
        builder.append('>');
        return builder.toString();
    }


    // Static builder methods

    public static BodyBuilder status(HttpStatusCode status) {
        Assert.notNull(status, "HttpStatusCode must not be null");
        return new DefaultBuilder(status);
    }

    public static BodyBuilder status(int status) {
        return new DefaultBuilder(status);
    }

    public static BodyBuilder ok() {
        return status(HttpStatus.OK);
    }

    public static <T> ResponseEntity<T> ok(T body) {
        return ok().body(body);
    }

    public static <T> ResponseEntity<T> of(Optional<T> body) {
        Assert.notNull(body, "Body must not be null");
        return body.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    public static <T> ResponseEntity<T> ofNullable(T body) {
        if (body == null) {
            return notFound().build();
        }
        return ResponseEntity.ok(body);
    }

    public static BodyBuilder created(URI location) {
        return status(HttpStatus.CREATED).location(location);
    }

    public static BodyBuilder accepted() {
        return status(HttpStatus.ACCEPTED);
    }

    public static HeadersBuilder<?> noContent() {
        return status(HttpStatus.NO_CONTENT);
    }

    public static BodyBuilder badRequest() {
        return status(HttpStatus.BAD_REQUEST);
    }

    public static HeadersBuilder<?> notFound() {
        return status(HttpStatus.NOT_FOUND);
    }

    public static BodyBuilder unprocessableEntity() {
        return status(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public static BodyBuilder internalServerError() {
        return status(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public interface HeadersBuilder<B extends HeadersBuilder<B>> {
        B header(String headerName, String... headerValues);

        B headers(HttpHeaders headers);
        B headers(Consumer<HttpHeaders> headersConsumer);

        B location(URI location);
        <T> ResponseEntity<T> build();
    }

    public interface BodyBuilder extends HeadersBuilder<BodyBuilder> {

        BodyBuilder contentLength(long contentLength);
        BodyBuilder contentType(MediaType contentType);

        <T> ResponseEntity<T> body(T body);
    }


    private static class DefaultBuilder implements BodyBuilder {

        private final HttpStatusCode statusCode;

        private final HttpHeaders headers = new HttpHeaders();


        public DefaultBuilder(int statusCode) {
            this(HttpStatusCode.valueOf(statusCode));
        }

        public DefaultBuilder(HttpStatusCode statusCode) {
            this.statusCode = statusCode;
        }


        @Override
        public BodyBuilder header(String headerName, String... headerValues) {
            for (String headerValue : headerValues) {
                this.headers.add(headerName, headerValue);
            }
            return this;
        }

        @Override
        public BodyBuilder headers(HttpHeaders headers) {
            if (headers != null) {
                this.headers.putAll(headers);
            }
            return this;
        }

        @Override
        public BodyBuilder headers(Consumer<HttpHeaders> headersConsumer) {
            headersConsumer.accept(this.headers);
            return this;
        }

        @Override
        public BodyBuilder contentLength(long contentLength) {
            this.headers.setContentLength(contentLength);
            return this;
        }

        @Override
        public BodyBuilder contentType(MediaType contentType) {
            this.headers.setContentType(contentType);
            return this;
        }

        @Override
        public BodyBuilder location(URI location) {
            this.headers.setLocation(location);
            return this;
        }


        @Override
        public <T> ResponseEntity<T> build() {
            return body(null);
        }

        @Override
        public <T> ResponseEntity<T> body(T body) {
            return new ResponseEntity<>(body, this.headers, this.statusCode);
        }
    }

}
