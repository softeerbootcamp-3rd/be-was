package webserver;

import java.io.*;
import java.net.Socket;
import java.util.*;

import DTO.Request;
import DTO.Response;
import HandlerMapping.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final int BUFFER_SIZE = 4096;
    private Socket connection;

    private static final List<String> contentType;
    static{
        contentType = new ArrayList<>();
    }

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = null;
            Request Request = MakeRequest(in);



            HandlerMapping handlerMapping = new HandlerMapping(Request);
            Response response = handlerMapping.Controller();

            response200Header(dos, response.Getbody().length, response.GetreturnType());
            responseBody(dos, response.Getbody());


            ///

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    ///

    private Request MakeRequest(InputStream inputStream) throws IOException {
        Request Request = new Request();
        // Read the request headers
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder requestBuilder = new StringBuilder();
        String line;
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

        Request.SetBody(requestBuilder.toString());

        System.out.println(requestBuilder.toString());
        // prints all the requests
        return Request;
    }
    private String getRequestMethod(String request) {
        // Extract the request method from the request headers
        return request.split(" ")[0];
    }


    private int getContentLength(String request) {
        // Extract the Content-Length from the request headers
        String[] headers = request.split("\r\n");
        for (String header : headers) {
            if (header.startsWith("Content-Length:")) {
                return Integer.parseInt(header.split(":")[1].trim());
            }
        }
        return 0;
    }


    ///

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            //dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
