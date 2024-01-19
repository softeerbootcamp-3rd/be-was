package util.http;

import util.MultiValueMap;

import java.net.URI;
import java.util.function.Consumer;


public class RequestEntity<T> extends HttpEntity<T> {
    private final HttpMethod method;
    private final URI url;

    public RequestEntity(HttpMethod method, URI url) {
        this(null, null, method, url);
    }

    public RequestEntity(T body, HttpMethod method, URI url) {
        this(body, null, method, url);
    }


    public RequestEntity(MultiValueMap<String, String> headers, HttpMethod method, URI url) {
        this(null, headers, method, url);
    }

    public RequestEntity(T body, MultiValueMap<String, String> headers,
                         HttpMethod method, URI url) {
        super(body, headers);
        this.method = method;
        this.url = url;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public URI getUrl() {
        if (this.url == null) {
            throw new UnsupportedOperationException(
                    "The RequestEntity was created with a URI template and variables, " +
                            "and there is not enough information on how to correctly expand and " +
                            "encode the URI template. This will be done by the RestTemplate instead " +
                            "with help from the UriTemplateHandler it is configured with.");
        }
        return this.url;
    }

    @Override
    public String toString() {
        return format(getMethod(), getUrl().toString(), getBody(), getHeaders());
    }

    static <T> String format(HttpMethod httpMethod, String url, T body, HttpHeaders headers) {
        StringBuilder builder = new StringBuilder("<");
        builder.append(httpMethod);
        builder.append(' ');
        builder.append(url);
        builder.append(',');
        if (body != null) {
            builder.append(body);
            builder.append(',');
        }
        builder.append(headers);
        builder.append('>');
        return builder.toString();
    }


    // Static builder methods

    public static BodyBuilder method(HttpMethod method, URI url) {
        return new DefaultBodyBuilder(method, url);
    }

    public static HeadersBuilder<?> get(URI url) {
        return method(HttpMethod.GET, url);
    }

    public static HeadersBuilder<?> head(URI url) {
        return method(HttpMethod.HEAD, url);
    }

    public static BodyBuilder post(URI url) {
        return method(HttpMethod.POST, url);
    }

    public static BodyBuilder put(URI url) {
        return method(HttpMethod.PUT, url);
    }

    public static BodyBuilder patch(URI url) {
        return method(HttpMethod.PATCH, url);
    }

    public static HeadersBuilder<?> delete(URI url) {
        return method(HttpMethod.DELETE, url);
    }

    public static HeadersBuilder<?> options(URI url) {
        return method(HttpMethod.OPTIONS, url);
    }


    public interface HeadersBuilder<B extends HeadersBuilder<B>> {

        B header(String headerName, String... headerValues);

        B headers(HttpHeaders headers);

        B headers(Consumer<HttpHeaders> headersConsumer);

        RequestEntity<Void> build();
    }


    public interface BodyBuilder extends HeadersBuilder<BodyBuilder> {

        BodyBuilder contentLength(long contentLength);

        BodyBuilder contentType(MediaType contentType);

        <T> RequestEntity<T> body(T body);
    }


    private static class DefaultBodyBuilder implements BodyBuilder {

        private final HttpMethod method;

        private final HttpHeaders headers = new HttpHeaders();

        private final URI uri;

        DefaultBodyBuilder(HttpMethod method, URI url) {
            this.method = method;
            this.uri = url;
        }

        DefaultBodyBuilder(HttpMethod method) {
            this.method = method;
            this.uri = null;

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
        public RequestEntity<Void> build() {
            return buildInternal(null);
        }

        @Override
        public <T> RequestEntity<T> body(T body) {
            return buildInternal(body);
        }

        private <T> RequestEntity<T> buildInternal(T body) {
            if (this.uri == null) {
                throw new IllegalStateException("Neither URI nor URI template");
            }
            return new RequestEntity<>(body, this.headers, this.method, this.uri);
        }
    }
}

