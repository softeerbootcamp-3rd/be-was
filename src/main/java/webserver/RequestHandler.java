package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import controller.MainController;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();

            if(line == null) {
                return;
            }
            logger.debug("--------------[Request Line]--------------");
            logger.debug(line);
            logger.debug("-------------[Request Header]-------------");

            String[] tokens = line.split(" ");
            String method = tokens[0];
            String url = tokens[1];
            HashMap<String, String> headers = new HashMap<>();
            final Set<String> printedKey = new HashSet<>(Arrays.asList("Accept",
                                                                        "Host",
                                                                        "User-Agent",
                                                                        "Cookie"));
            while(true) {
                line = br.readLine();
                if(line.isEmpty()) break;
                String[] keyAndValue = line.split(": ");
                String key = keyAndValue[0], value = keyAndValue[1];
                if(printedKey.contains(key)) logger.debug(line);
                headers.put(key, value);
            }
            String types[] = headers.get("Accept").split(",");
            String mimeType = types[0];
            int index = url.indexOf("?");
            MainController controller;
            if(index != -1) { // 파라미터가 존재
                String queryString = url.substring(index + 1);
                HashMap<String, String> params = queryStringParsing(queryString);

                controller = new MainController(method, url.substring(0, index), headers, params);
            }
            else {
                controller = new MainController(method, url, headers);
            }
            byte[] body = controller.control();
            response200Header(dos, mimeType, body.length);
            responseBody(dos, body);
            logger.debug("\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos,
                                   String mimeType,
                                   int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + mimeType + ";charset=utf-8\r\n");
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

    public static HashMap<String, String> queryStringParsing(String queryString) {
        HashMap<String, String> queries = new HashMap<>();

        String[] keyAndValue = queryString.split("&");
        for(String keyValue : keyAndValue) {
            int indexOfEqual = keyValue.indexOf("=");
            String key = keyValue.substring(0, indexOfEqual);
            String value = keyValue.substring(indexOfEqual+1);
            queries.put(key, value);
        }
        return queries;
    }
}
