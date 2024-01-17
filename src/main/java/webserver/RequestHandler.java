package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.util.Map;

import controller.UserController;
import dto.UserCreateRequestDto;
import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.HttpResponse.*;
import static util.UrlParser.parseQueryString;

public class RequestHandler implements Runnable {
    private static final String USER_PATH = "/user";
    private final Socket connection;
    private static final UserController userController = new UserController();
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

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

        while (!line.isEmpty()) {
            line = br.readLine();
            request.addHeader(line);
        }
        return request;
    }

    private Response handleRequest(Request request, DataOutputStream dos) throws IOException {
        String url = request.getUrl();
        if (url.startsWith(USER_PATH)) {
            return handleUserPath(url.substring(USER_PATH.length()), request, dos);
        }
        return new Response(200, serveStaticResource(request));
    }

    private Response handleUserPath(String url, Request request, DataOutputStream dos) throws IOException {
        if (url.startsWith("/create")) {
            return handleUserCreation(request, url.substring("/create".length()), dos);
        }
        return new Response(200, serveStaticResource(request));
    }

    private Response handleUserCreation(Request request, String query, DataOutputStream dos) {
        try {
            if (!query.startsWith("?")) {
                return new Response(400, "잘못된 쿼리 문자열".getBytes());
            }

            String decodedQuery = URLDecoder.decode(query.substring(1), StandardCharsets.UTF_8);
            Map<String, String> queryParams = parseQueryString(decodedQuery);
            UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(queryParams.get("userId"),
                    queryParams.get("password"),
                    queryParams.get("name"),
                    queryParams.get("email"));
            userController.create(userCreateRequestDto);
            return new Response(302, new byte[0], "/index.html");

        } catch (Exception e) {
            logger.error("User creation failed : " + e.getMessage(), e);
            return new Response(500, "서버 내부 오류".getBytes());
        }
    }


    private byte[] serveStaticResource(Request request) throws IOException {
        String filePath = request.getFilePath();
        File file = new File(filePath);
        if (!file.exists()) {
            return "404 File Not Found".getBytes();
        }
        return Files.readAllBytes(file.toPath());
    }

    private void sendResponse(Request request, Response response, DataOutputStream dos) throws IOException {
        if (response.getStatusCode() == 302) {
            responseRedirectHeader(dos, request, response.getRedirectUrl());
        } else {
            sendGeneralResponse(response.getStatusCode(), response.getBody(), request, dos);
        }
        responseBody(response.getBody(), dos);
    }

    private void sendGeneralResponse(int statusCode, byte[] body, Request request, DataOutputStream dos) throws IOException {
        // 상태 코드에 따른 헤더 설정
        switch (statusCode) {
            case 200:
                response200Header(dos, request, body.length);
                break;
            case 400:
                responseBadRequest(dos, request, new String(body, StandardCharsets.UTF_8));
                break;
            case 500:
                responseInternalServerError(dos, request, new String(body, StandardCharsets.UTF_8));
                break;
            // 다른 상태 코드 처리
        }
    }
    private void responseBody(byte[] body, DataOutputStream dos) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}