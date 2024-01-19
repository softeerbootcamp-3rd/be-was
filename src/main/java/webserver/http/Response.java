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
    private final HashMap<String, String> responseHeader= new HashMap<>();
    private final ResponseHandler responseHandler = new ResponseHandler();
    private byte[] responseBody;

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
        Optional.ofNullable(request.getRequestHeader().get("Location"))
                .ifPresent(location -> responseHeader.put("Location", location));
    }

    public void setStatusCode(Request request) {
        if(request.getRequestHeader().get("Location")!=null){
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
    }

    public byte[] getResponseBody() {
        return responseBody;
    }
}
