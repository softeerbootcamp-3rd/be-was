package webserver.http;

import java.io.DataOutputStream;
import java.util.HashMap;

public class Response {

    String httpVersion;
    StatusCode statusCode;
    String statusText;
    HashMap<String, String> responseHeader;
    HashMap<String, String> responseBody;

    public Response(DataOutputStream dos, byte[] body) {
    }
}
