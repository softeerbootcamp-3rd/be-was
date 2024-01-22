package webserver.routing;

import utils.FileUtil;
import webserver.http.request.HttpRequest;
import webserver.http.response.ContentType;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;

import java.io.IOException;

public class StaticRoutingManager {
    public static HttpResponse handleRequest(HttpRequest httpRequest) throws IOException {
        String path = httpRequest.getPath();
        byte[] body = FileUtil.getFileContents(path);

        if (body != null) {
            String fileExtension = FileUtil.getFileExtension(path);
            ContentType contentType = ContentType.toContentType(fileExtension);

            HttpResponse httpResponse = new HttpResponseBuilder().createSuccessResponse(HttpStatus.OK, body);
            httpResponse.addHeader("Content-Type", contentType.getMimeType());

            return httpResponse;
        }
        return null;
    }
}
