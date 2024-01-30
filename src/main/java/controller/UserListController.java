package controller;

import httpmessage.HttpStatusCode;
import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;

public class UserListController implements Controller {
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {


        httpResponse.setHttpStatusCode(HttpStatusCode.MOVED_TEMPORARILY);

        if (!httpRequest.getCookie().isEmpty()) {
            httpResponse.setRedirectionPath("/user/list.html");
        }

        else{
            httpResponse.setRedirectionPath("/user/login.html");
        }
    }
}
