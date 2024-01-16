package webserver.http;

import java.util.ArrayList;
import java.util.HashMap;

public class Request {
    String httpMethod;
    String requestTarget;
    String httpVersion;
    Float httpVersionNum;
    HashMap<String, String> requestHeader;
    HashMap<String, String> requestBody;

    public Request(ArrayList<String> requestHeaderContent) {
    }
}
