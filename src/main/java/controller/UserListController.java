package controller;

import db.Database;
import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import httpmessage.response.ResponsePasing;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import java.io.IOException;
import java.util.Collection;

public class UserListController implements Controller {
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        int statusCode = 302;
        httpResponse.setPath("/user/list.html");
        httpResponse.setStatusCode(statusCode);

    }
}
