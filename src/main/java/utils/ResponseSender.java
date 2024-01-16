package utils;

import java.io.DataOutputStream;

public class ResponseSender {
    public static void sendResponse(DataOutputStream dos, int lengthOfBodyContent, byte[] body){
        ResponseHandler.response200Header(dos, lengthOfBodyContent);
        ResponseHandler.responseBody(dos, body);
    }
}
