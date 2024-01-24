package webserver.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.RequestHandler;
import webserver.request.Request;
import webserver.response.Response;
import webserver.type.ContentType;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class ResourceAdapter implements Adapter{
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static final URL HTML_BASE_URL = ResourceAdapter.class.getClassLoader().getResource("./templates");
    private static final URL OTHERS_BASE_URL = ResourceAdapter.class.getClassLoader().getResource("./static");
    private static final ResourceAdapter resourceAdapter = new ResourceAdapter();

    private ResourceAdapter(){}

    public static ResourceAdapter getInstance(){
        return resourceAdapter;
    }

    public Response run(Request request) throws IOException {
        String path = request.getPath();
        String requestFileExtension = getRequestFileExtension(path);

        logger.debug(requestFileExtension);

        ContentType contentType = ContentType.findContentType(requestFileExtension);

        if(contentType == null){
            return null;
        }

        File file = getRequestFile(path, requestFileExtension);

        return makeResponse(file, contentType);
    }

    @Override
    public boolean canRun(Request request) {
        if(request.getMethod().equals("GET")){
            return new File(HTML_BASE_URL.getPath() + request.getPath()).exists() ||
                   new File(OTHERS_BASE_URL.getPath() + request.getPath()).exists();
        }

        return false;
    }

    private static Response makeResponse(File file, ContentType contentType) throws IOException {
        if(!file.isDirectory() && file.exists()) {

            return Response.onSuccess(Files.readAllBytes(file.toPath()), contentType);
        }

        return null;
    }

    private static File getRequestFile(String path, String requestFileExtension){
        if(requestFileExtension.equals("html")){
            return new File(HTML_BASE_URL.getPath() + path);
        }

        return new File(OTHERS_BASE_URL.getPath() + path);
    }

    private static String getRequestFileExtension(String path){
        return path.substring(path.lastIndexOf(".") + 1);
    }
}
