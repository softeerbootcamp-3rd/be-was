package webserver.http;

import webserver.http.constants.StatusCode;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;

public class Response {
    private String httpVersion;
    private int statusCode;
    private String statusText;
    private final HashMap<String, String> responseHeader= new HashMap<>();
    private final ResponseHandler responseHandler = new ResponseHandler();
    private byte[] responseBody;
    private final String locString = "Location";
    private final String cookieString = "Set-Cookie";
    private final DynamicResourceHandler dynamicResourceHandler = new DynamicResourceHandler();

    public Response(Request request) {
        this.httpVersion = request.getHttpVersion();
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

        if(request.getRequestHeader().get("Error")!=null){
            StatusCode errorStatusCode = StatusCode.fromString(request.getRequestHeader().get("Error"));
            this.statusCode = errorStatusCode.getCode();
            this.statusText = errorStatusCode.name();
            return;
        }

        this.statusCode = StatusCode.OK.getCode();
        this.statusText = StatusCode.OK.name();
    }

    public void setStatusCode(StatusCode statusCode){
        this.statusCode = statusCode.getCode();
        this.statusText = statusCode.name();
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

    private void setBody(Request request){
        String requestTarget = request.getRequestTarget().split("\\?")[0];
        responseBody = responseHandler.setResponseBody(request.getResponseMimeType(), requestTarget, this);
        dynamicResourceHandler.handle(request, this);
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }
}
