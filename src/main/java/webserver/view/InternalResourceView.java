package webserver.view;

import http.HttpContentType;
import http.HttpStatus;
import http.Request;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class InternalResourceView implements View{
    private static final Logger logger = LoggerFactory.getLogger(InternalResourceView.class);
    private String viewPath;
    public InternalResourceView(String viewPath) {
        this.viewPath = viewPath;
    }
    @Override
    public String getContentType() {
        int dotIndex = viewPath.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < viewPath.length() - 1) {
            String fileExtension = viewPath.substring(dotIndex + 1);
            return HttpContentType.getValue(fileExtension);
        }
        return null;
    }
    @Override
    public void render(Request request, Response response){
        try {
            forward(request, response, viewPath);
        } catch (IOException e){
            return;
        }
    }

    public String getViewPath() {
        return viewPath;
    }

    private void forward(Request request, Response response, String viewPath) throws IOException {
        File file = new File(viewPath);
        byte[] body;
        if (file.exists() && file.isFile()) {
            body = Files.readAllBytes(file.toPath());
            response.setStatus(HttpStatus.OK);
        }
        else{
            body = Files.readAllBytes(new File("/not-found.html").toPath());
            response.setStatus(HttpStatus.NOT_FOUND);
        }
        response.send(body,request);
    }

}
