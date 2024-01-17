package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Request;
import webserver.http.Response;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private static final String ROOT_DIRECTORY = System.getProperty("user.dir");

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        // in-> request, out -> response
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Request request = new Request(br);

            DataOutputStream dos = new DataOutputStream(out);

            byte[] body = Files.readAllBytes(new File(ROOT_DIRECTORY + "/src/main/resources/templates" + request.getRequestTarget()).toPath());

            Response response = new Response(request, body);

            sendResponse(dos, body, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendResponse(DataOutputStream dos, byte[] body, Response response) {
        try{
            dos.writeBytes(response.getHttpVersion() + " " + response.getStatusCode() + " " + response.getStatusText() + "\r\n");
            for (Map.Entry<String, String> entry : response.getResponseHeader().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                dos.writeBytes(key + ": " + value + "\r\n");
            }

            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();
        }catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
