package webserver.http;

import java.io.DataOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

public class Response {

    String httpVersion;
    int statusCode;
    String statusText;
    HashMap<String, String> responseHeader;
    //추후 구현할 내용
    HashMap<String, String> responseBody;

    public Response(Request request, byte[] body) {
        this.httpVersion = request.httpVersion;
        setStatusCode(request);
        setHeader(body.length);
    }

    private void setHeader(int bodyLength) {
        responseHeader.put("Date",ZonedDateTime.now().format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)));
        responseHeader.put("Server", "MyServer/1.0" );
        responseHeader.put("Content-Length", Integer.toString(bodyLength));
        responseHeader.put("Content-Type", "text/html; charset=UTF-8");
    }

    public void setStatusCode(Request request) {
        //PathTraversalAttack
        if(request.requestTarget.contains("../"))
        {
            this.statusCode = StatusCode.BAD_REQUEST.getCode();
            this.statusText = StatusCode.BAD_REQUEST.name();
            return;
        }

        this.statusCode = StatusCode.OK.getCode();
        this.statusText = StatusCode.OK.name();
    }
}
