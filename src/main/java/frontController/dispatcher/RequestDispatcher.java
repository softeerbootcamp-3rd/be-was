package frontController.dispatcher;

import http.Request;
import http.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public interface RequestDispatcher {
    void forward(Request request, Response response, String viewPath, DataOutputStream dos) throws IOException;

    void sendRedirect(Request request, Response response, String viewPath, DataOutputStream dos);
}
