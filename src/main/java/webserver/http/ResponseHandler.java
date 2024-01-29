package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ResponseHandler {

    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);
    private static final String ROOT_DIRECTORY = System.getProperty("user.dir");
    private static final String TEMPLATE_DIRECTORY ="/src/main/resources/templates";
    private static final String STATIC_DIRECTORY ="/src/main/resources/static";
    public byte[] setResponseBody(Mime responseMimeType, String requestTarget) {
        byte[] responseBody = new byte[0];
        try {
            File file = null;
            if (responseMimeType == Mime.TEXT_HTML) {
                file = new File(ROOT_DIRECTORY + TEMPLATE_DIRECTORY + requestTarget);
            } else if (responseMimeType == Mime.NONE) {
                responseBody = new byte[0];
            } else {
                file = new File(ROOT_DIRECTORY + STATIC_DIRECTORY + requestTarget);
            }

            if (file != null && file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                responseBody = new byte[(int) file.length()];
                fis.read(responseBody);
                fis.close();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return responseBody;
    }
}
