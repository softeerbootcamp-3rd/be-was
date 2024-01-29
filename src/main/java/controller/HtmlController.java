package controller;

import db.Database;
import httpmessage.HttpSession;
import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import model.Content;
import model.User;

import java.util.Collection;

public class HtmlController implements Controller{
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        final String requestPath =  httpRequest.getPath();
        String body = new String(httpResponse.getBody());

        Content loginContent = new Content(httpRequest,httpResponse);
        String topContent = loginContent.getString();
        String modifiedContent = body.replace("{{login}}",topContent);

        if(requestPath.contains("list")){
            Database database = new Database();
            Collection<User> users = database.findAll();
            Content listContent = new Content(users);

            String userConent = listContent.getString();
            modifiedContent = body.replace("{{list}}",userConent);
        }
        byte[] modifiedBody = modifiedContent.getBytes();
        httpResponse.setBody(modifiedBody);
    }
}

