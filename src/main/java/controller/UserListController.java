package controller;

import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import httpmessage.response.ResponsePasing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import java.io.IOException;

public class UserListController implements Controller {
    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        ResponsePasing responsePasing = new ResponsePasing();
        int statusCode = 302;
        httpResponse.setPath("/user/list.html");
        httpResponse.setStatusCode(statusCode);

    }
}
