package webserver.routing;

import utils.FileUtil;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;

import java.io.IOException;

public class StaticRoutingManager {
    public static HttpResponse handleRequest(HttpRequest httpRequest) throws IOException {
        String path = httpRequest.getPath();
        byte[] body = FileUtil.getFileContents(path);

        if (body != null) {
            return new HttpResponseBuilder().createSuccessResponse(HttpStatus.OK, body);
        }
        return null;
    }
}
