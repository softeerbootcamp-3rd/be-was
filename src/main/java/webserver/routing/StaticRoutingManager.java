package webserver.routing;

import utils.ResourceReader;
import webserver.http.request.HttpRequest;
import webserver.http.response.enums.ContentType;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.enums.HttpStatus;

import java.io.IOException;

public class StaticRoutingManager {
    public static final byte[] NOT_FOUND_MESSAGE = "The requested resource was not found on this server.".getBytes();
    private static final StaticRoutingManager instance = new StaticRoutingManager();

    private StaticRoutingManager(){}

    public static StaticRoutingManager getInstance(){
        return instance;
    }

    public HttpResponse handleRequest(HttpRequest httpRequest) throws IOException {
        String path = httpRequest.getPath();
        byte[] body = ResourceReader.getInstance().getFileContents(path);

        if (body != null) {
            String fileExtension = ResourceReader.getInstance().getFileExtension(path);
            ContentType contentType = ContentType.toContentType(fileExtension);

            HttpResponse httpResponse = HttpResponseBuilder.getInstance().createSuccessResponse(HttpStatus.OK, body);
            httpResponse.addHeader("Content-Type", contentType.getMimeType());

            return httpResponse;
        }
        return HttpResponseBuilder.getInstance().createErrorResponse(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE);
    }
}
