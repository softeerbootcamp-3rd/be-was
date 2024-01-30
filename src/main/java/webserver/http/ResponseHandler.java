package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.constants.Mime;
import webserver.http.constants.StatusCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ResponseHandler {

    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);
    private static final String ROOT_DIRECTORY = System.getProperty("user.dir");
    private static final String TEMPLATE_DIRECTORY ="/src/main/resources/templates";
    private static final String STATIC_DIRECTORY ="/src/main/resources/static";
    public byte[] setResponseBody(Mime responseMimeType, String requestTarget, Response response) {
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

            if(response.getStatusCode() == 405){
                //405 error 처리
                file = new File(ROOT_DIRECTORY + TEMPLATE_DIRECTORY + "/error/405.html");
                FileInputStream fis = new FileInputStream(file);
                responseBody = new byte[(int) file.length()];
                fis.read(responseBody);
                fis.close();
                response.setStatusCode(StatusCode.NOT_FOUND);
                return responseBody;
            }

            if (file != null && file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                responseBody = new byte[(int) file.length()];
                fis.read(responseBody);
                fis.close();
            }
            else if(responseMimeType != Mime.NONE){
                //404 error 처리
                file = new File(ROOT_DIRECTORY + TEMPLATE_DIRECTORY + "/error/404.html");
                FileInputStream fis = new FileInputStream(file);
                responseBody = new byte[(int) file.length()];
                fis.read(responseBody);
                fis.close();
                response.setStatusCode(StatusCode.NOT_FOUND);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        try {
            String string = new String(responseBody, "UTF-8");
            // Use the string as needed
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // Handle the exception, maybe use a different encoding
        }

        return responseBody;
    }
}
