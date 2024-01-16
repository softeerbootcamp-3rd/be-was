package frontController.dispatcher;

import dto.Request;
import dto.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public interface RequestDispatcher {
    void forward(Request request, Response response, String viewPath, DataOutputStream dos) throws IOException;

    void sendRedirect(Request request, Response response, String viewPath, DataOutputStream dos);
}
