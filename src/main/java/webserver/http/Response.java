package webserver.http;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;

public class Response {
    private String httpVersion;
    private int statusCode;
    private String statusText;
    private String requestTarget;
    private final HashMap<String, String> responseHeader= new HashMap<>();
    private final ResponseHandler responseHandler = new ResponseHandler();
    private byte[] responseBody;
    private final String locString = "Location";
    private final String cookieString = "Set-Cookie";
    private final DynamicResourceHandler dynamicResourceHandler = new DynamicResourceHandler();

    public Response(Request request) {
        this.httpVersion = request.getHttpVersion();
        this.requestTarget = request.getRequestTarget();
        setStatusCode(request);
        setBody(request);
        setHeader(request);
    }
    private void setHeader(Request request) {
        responseHeader.put("Date",ZonedDateTime.now().format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)));
        responseHeader.put("Server", "MyServer/1.0" );
        responseHeader.put("Content-Length", Integer.toString(responseBody.length));
        responseHeader.put("Content-Type", request.getRequestHeader().get("Accept").split(",")[0]);
        Optional.ofNullable(request.getRequestHeader().get(locString))
                .ifPresent(location -> responseHeader.put(locString, location));
        Optional.ofNullable(request.getRequestHeader().get(cookieString))
                .ifPresent(location -> responseHeader.put(cookieString, location));
    }
    public void setStatusCode(Request request) {
        if(request.getRequestHeader().get(locString)!=null){
            this.statusCode = StatusCode.FOUND.getCode();
            this.statusText = StatusCode.FOUND.name();
            return;
        }

        this.statusCode = StatusCode.OK.getCode();
        this.statusText = StatusCode.OK.name();
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public HashMap<String, String> getResponseHeader() {
        return responseHeader;
    }

    void setBody(Request request){
        responseBody = responseHandler.setResponseBody(request.getResponseMimeType(), request.getRequestTarget());
        dynamicResourceHandler.handle(this);
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }

    public String getRequestTarget() {
        return requestTarget;
    }
}
