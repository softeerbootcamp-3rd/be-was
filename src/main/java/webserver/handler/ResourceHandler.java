package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.header.RequestHeader;
import webserver.response.Response;
import webserver.type.ContentType;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class ResourceHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static final URL HTML_BASE_URL = ResourceHandler.class.getClassLoader().getResource("./templates");
    private static final URL OTHERS_BASE_URL = ResourceHandler.class.getClassLoader().getResource("./static");

    public static Response run(RequestHeader requestHeader) throws IOException {
        String path = requestHeader.getPath();
        String requestFileExtension = getRequestFileExtension(path);

        logger.debug(requestFileExtension);

        ContentType contentType = ContentType.findContentType(requestFileExtension);

        if(contentType == null){
            return null;
        }

        File file = getRequestFile(path, requestFileExtension);

        return makeResponse(file, contentType);
    }

    private static Response makeResponse(File file, ContentType contentType) throws IOException {
        if(!file.isDirectory() && file.exists()) {
            return Response.onSuccess(contentType, Files.readAllBytes(file.toPath()));
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
