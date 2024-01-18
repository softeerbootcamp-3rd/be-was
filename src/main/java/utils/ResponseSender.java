package utils;

import java.io.DataOutputStream;

public class ResponseSender {
    public static void sendResponse(DataOutputStream dos, int lengthOfBodyContent, byte[] body){
        ResponseHandler.response200Header(dos, lengthOfBodyContent);
        ResponseHandler.responseBody(dos, body);
    }

    public static void sendRedirectResponse(DataOutputStream dos, String redirectLocation){
        ResponseHandler.response302Header(dos, redirectLocation);
    }
}
