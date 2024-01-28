package controller;

import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;

public class UserListController implements Controller {
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {


        int statusCode = 302;
        httpResponse.setStatusCode(statusCode);

        if (!httpRequest.getCookie().isEmpty()) {
            httpResponse.setRedirectionPath("/user/list.html");
        }

        else{
            httpResponse.setRedirectionPath("/user/login.html");
        }
    }
}
