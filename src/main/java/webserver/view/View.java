package webserver.view;

import http.Request;
import http.Response;

import java.io.DataOutputStream;
import java.util.Map;

public interface View {
    String getContentType();
    void render(DataOutputStream dos, Request request, Response response) throws Exception;
}
