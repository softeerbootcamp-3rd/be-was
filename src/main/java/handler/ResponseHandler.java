package handler;

import http.request.HttpRequest;
import http.response.ContentType;
import http.response.HttpResponse;
import http.response.HttpResponseHeader;
import http.response.HttpResponseStartLine;
import http.response.HttpStatusCode;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import logger.CustomLogger;

public class ResponseHandler {

    private ResponseHandler() {
    }

    private static class SingletonHelper {

        private static final ResponseHandler SINGLETON = new ResponseHandler();
    }

    public static ResponseHandler getInstance() {
        return SingletonHelper.SINGLETON;
    }

    public void process(OutputStream out, byte[] body, HttpRequest httpRequest) {
        DataOutputStream dos = new DataOutputStream(out);

        HttpResponse httpResponse = createResponse(body, httpRequest);

        try {
            dos.write(httpResponse.convertResponseToByteArray(), 0,
                httpResponse.convertResponseToByteArray().length);
            dos.flush();
        } catch (IOException e) {
            CustomLogger.printError(e);
        }
    }

    private HttpResponse createResponse(byte[] body, HttpRequest httpRequest) {
        HashMap<String, String> responseHeader = new HashMap<>();
        responseHeader.put("Content-Length", String.valueOf(body.length));
        responseHeader.put("Content-Type", getContentType(httpRequest));

        return new HttpResponse(
            new HttpResponseStartLine(HttpStatusCode.OK),
            new HttpResponseHeader(responseHeader),
            body
        );
    }

    private String getContentType(HttpRequest httpRequest) {
        String requestTarget = httpRequest.getHttpRequestStartLine().getRequestTarget();
        String fileExtension = getFileExtension(requestTarget);

        ContentType contentType = ContentType.getByFileExtension(fileExtension);
        return contentType.getValue();
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }


}
