package http;

import java.io.File;

public class ResponseHeaderMaker {

    public void setHttpResponse(HttpResponse response) {
        String path = response.getPath();

        if (!path.startsWith("redirect:") && !isFileExists(path)) path=null;

        if (path == null) {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
        } else if (path.startsWith("redirect:")) {
            response.setHttpStatus(HttpStatus.FOUND);
            response.setHeader("Location", path.substring("redirect:".length()));
        } else if (path.contains("error")) {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
        } else {
            response.setHttpStatus(HttpStatus.OK);
        }
    }

    private boolean isFileExists(String path) {
        return new File(path).exists();
    }
}
