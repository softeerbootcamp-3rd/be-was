package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);
    private static final String ROOT_DIRECTORY = System.getProperty("user.dir");
    private static final String TEMPLATE_DIRECTORY ="/src/main/resources/templates";
    private static final String STATIC_DIRECTORY ="/src/main/resources/static";
    public byte[] setResponseBody(Mime responseMimeType, String requestTarget) {
        byte[] responseBody;
        try{
            if(responseMimeType==Mime.TEXT_HTML){
                responseBody =  Files.readAllBytes(new File(ROOT_DIRECTORY + TEMPLATE_DIRECTORY + requestTarget).toPath());
            }
            else if(responseMimeType == Mime.NONE)
                responseBody = new byte[0];
            else{
                responseBody = Files.readAllBytes(new File(ROOT_DIRECTORY + STATIC_DIRECTORY + requestTarget).toPath());
            }
        }
        catch (IOException e) {
            logger.error(e.getMessage());
            responseBody = new byte[0];
        }
        return responseBody;
    }
}
