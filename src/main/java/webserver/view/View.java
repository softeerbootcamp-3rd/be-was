package webserver.view;

import http.Request;
import http.Response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public interface View {
    String getContentType();
    void render(Request request, Response response);
}
