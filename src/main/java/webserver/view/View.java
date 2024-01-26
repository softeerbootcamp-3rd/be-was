package webserver.view;

import http.Request;
import http.Response;
import webserver.Model;

public interface View{
    String getContentType();
    void render(Request request, Response response, Model model);
}
