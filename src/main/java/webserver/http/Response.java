package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);
    private static final String ROOT_DIRECTORY = System.getProperty("user.dir");
    String httpVersion;
    int statusCode;
    String statusText;
    HashMap<String, String> responseHeader;
    byte[] responseBody;

    public Response(Request request) {
        this.httpVersion = request.httpVersion;
        this.responseHeader = new HashMap<>();
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
        try{
            if(request.responseMimeType==Mime.TEXT_HTML){
                responseBody = Files.readAllBytes(new File(ROOT_DIRECTORY + "/src/main/resources/templates" + request.getRequestTarget()).toPath());
            }
            else if(request.responseMimeType == Mime.NONE)
                responseBody = new byte[0];
            else{
                responseBody = Files.readAllBytes(new File(ROOT_DIRECTORY + "/src/main/resources/static" + request.getRequestTarget()).toPath());
                System.out.println(ROOT_DIRECTORY + "/src/main/resources/static" + request.getRequestTarget());
            }
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    public byte[] getResponseBody() {
        return responseBody;
    }
}
