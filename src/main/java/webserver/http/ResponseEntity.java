package webserver.http;

import java.util.List;
import java.util.Map;

public class ResponseEntity<T> extends HttpEntity<T> {

    private HttpStatus httpStatus;

    public ResponseEntity(HttpStatus httpStatus) {
        this(httpStatus, (HttpHeader) null, null);
    }

    public ResponseEntity(HttpStatus httpStatus, Map<String, List<String>> headers) {
        this(httpStatus, headers, null);
    }


    public ResponseEntity(HttpStatus httpStatus, Map<String, List<String>> headers, T body) {
        super(headers, body);
        this.httpStatus = httpStatus;
    }
    public ResponseEntity(HttpStatus httpStatus, HttpHeader headers, T body) {
        super(headers, body);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!super.equals(other)) {
            return false;
        }
        ResponseEntity<?> otherEntity = (ResponseEntity<?>) other;
        return this.httpStatus.equals(otherEntity.httpStatus) ? true : false;
    }

    // <200 OK OK,ResponseDto(success=true, code=0, message=Ok),[]>
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<");
        builder.append(this.httpStatus.getStatusCode());
        if (this.httpStatus instanceof HttpStatus) {
            builder.append(' ');
            builder.append(httpStatus.getStatusMessage());
        }
        builder.append(',');
        T body = getBody();
        HttpHeader header = getHeaders();
        if (body != null) {
            builder.append(body);
            builder.append(',');
        }
        builder.append(header);
        builder.append('>');
        return builder.toString();
    }

}