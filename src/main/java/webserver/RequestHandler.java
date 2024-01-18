package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import model.HttpStatus;
import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.HttpResponse.*;

public class RequestHandler implements Runnable {
    private static final String USER_PATH = "/user";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    private final UserHandler userHandler = new UserHandler();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            Request request = getRequest(in);
            Response response = handleRequest(request, dos);
            sendResponse(request, response, dos);
        } catch (IllegalArgumentException | IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static Request getRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        String line = br.readLine();
        Request request = new Request(line);
        logger.debug("request : {}", request.toString());

        while ((line = br.readLine()) != null && !line.isEmpty()) {
            request.addHeader(line);
        }
        return request;
    }

    private Response handleRequest(Request request, DataOutputStream dos) throws IOException {
        String url = request.getUrl();
        if (url.startsWith(USER_PATH)) {
            return userHandler.handleUserPath(url.substring(USER_PATH.length()), request, dos);
        }
        return new Response(HttpStatus.OK, serveStaticResource(request));
    }

    public static byte[] serveStaticResource(Request request) throws IOException {
        String filePath = request.getFilePath();
        File file = new File(filePath);
        if (!file.exists()) {
            return "404 File Not Found".getBytes();
        }
        return Files.readAllBytes(file.toPath());
    }

    private void sendResponse(Request request, Response response, DataOutputStream dos) throws IOException {
        if (response.getStatus() == HttpStatus.REDIRECT) {
            responseRedirectWithoutBody(dos, request, response);
            return;
        }
        if (response.getStatus() == HttpStatus.OK) {
            responseOKWithBody(dos, request, response);
            return;
        }
        responseWithoutBody(dos, request, response);
    }
}