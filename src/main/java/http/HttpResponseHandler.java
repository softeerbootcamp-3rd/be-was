package http;

public class HttpResponseHandler {

    public void setHttpResponse(HttpResponse response) {
        String path = response.getPath();

        if (path == null) {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
        } else if (path.startsWith("redirect:")) {
            response.setHttpStatus(HttpStatus.FOUND);
            response.setHeader("Location", path.substring("redirect:".length()));
        } else {
            response.setHttpStatus(HttpStatus.OK);
        }

    }
}
