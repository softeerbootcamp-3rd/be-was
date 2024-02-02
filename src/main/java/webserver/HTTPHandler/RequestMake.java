package webserver.HTTPHandler;

import HTTPModel.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RequestMake {

    private static final int BUFFER_SIZE = 4096;

    public RequestMake(){};

    public static Request MakeRequest(InputStream inputStream) throws IOException {
        Request Request = new Request();
        // Read the request headers
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        String sid;

        line = reader.readLine();
        requestBuilder.append(line).append("\r\n");
        Request.SetHTTPMethod(line.split("\\s+")[0]);
        Request.SetURI(line.split("\\s+")[1]);
        Request.SetVersion(line.split("\\s+")[2]);




        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            requestBuilder.append(line).append("\r\n");
            if("Accept".equals(line.split(":")[0])){
                String[] acceptContentsString  = line.split(":")[1].split(",");

                for(String accept : acceptContentsString){
                    Request.SetAccept(accept.trim().split(";")[0]);
                }
            }
            else if("Cookie".equals(line.split(":")[0])){

                String[] cookieContentsString  = line.split(":")[1].trim().split(";");
                for(String token : cookieContentsString){
                    String head = token.trim().split("=")[0];
                    if("sid".equals(head)){
                        sid = token.split("=")[1];
                        Request.SetSid(sid);
                    }
                }

            }
        }

        StringBuilder bodyJson = new StringBuilder();
        // Read the request body if present
        if ("POST".equals(getRequestMethod(requestBuilder.toString()))) {
            int contentLength = getContentLength(requestBuilder.toString());
            char[] buffer = new char[BUFFER_SIZE];
            int bytesRead;
            while (contentLength > 0 && (bytesRead = reader.read(buffer, 0, Math.min(contentLength, BUFFER_SIZE))) != -1) {
                requestBuilder.append(buffer, 0, bytesRead);
                bodyJson.append(buffer, 0, bytesRead);
                contentLength -= bytesRead;
            }
        }

        Request.SetBody(bodyJson.toString());


        System.out.println(requestBuilder.toString());

        return Request;
    }
    private static String getRequestMethod(String request) {
        // Extract the request method from the request headers
        return request.split(" ")[0];
    }


    private static int getContentLength(String request) {
        // Extract the Content-Length from the request headers
        String[] headers = request.split("\r\n");
        for (String header : headers) {
            if (header.startsWith("Content-Length:")) {
                return Integer.parseInt(header.split(":")[1].trim());
            }
        }
        return 0;
    }

}
