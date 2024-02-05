package http;

import exception.NotSupportedContentTypeException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.MainPageView;
import webserver.ContentType;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static http.HttpStatus.*;
import static webserver.ContentType.NONE;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private static final String ROOT_PATH = "src/main/resources";
    private static final String INDEX = "/index.html";
    private static final String ERROR_PAGE = "src/main/resources/templates/error.html";
    private static final String MAIN_PAGE = "src/main/resources/templates" + INDEX;

    private byte[] body;

    private HttpStatus status;
    private String filePath;
    private List<Cookie> cookies = new ArrayList<>();

    private HttpResponse(HttpStatus status, String filePath) {
        this.status = status;
        this.filePath = filePath;
    }

    public static HttpResponse redirect() {
        return new HttpResponse(FOUND, INDEX);
    }

    public static HttpResponse init(String filePath) {
        return new HttpResponse(OK, filePath);
    }

    public static HttpResponse ok(String filePath) {
        return new HttpResponse(OK, filePath);
    }

    public String getFilePath() {
        return filePath;
    }

    //리소스 경로 매핑
    public void mapResourcePath(ContentType type) {
        if (type == NONE) {
            throw new NotSupportedContentTypeException();
        }
        this.filePath = ROOT_PATH + type.getPath() + this.filePath;
    }

    //사용자 정보로 응답세팅
    public void setResponse(User user) throws IOException {
        byte[] body = convertFilePathToBody(user);
        this.body = body;
    }

    public void setResponse(HttpStatus status) throws IOException {
        this.status = status;
        byte[] body = convertFilePathToBody();
        this.body = body;
    }

    //파일 경로
    private byte[] convertFilePathToBody() throws IOException {
        handlePathByHttpStatus();
        return Files.readAllBytes(new File(filePath).toPath());
    }

    private byte[] convertFilePathToBody(User user) throws IOException {
        handlePathByHttpStatus();
        if(isMainPage()){
            MainPageView page = MainPageView.from(user);
            return page.getByteArray();
        }
        return Files.readAllBytes(new File(filePath).toPath());
    }

    private boolean isMainPage() {
        return MAIN_PAGE.equals(filePath);
    }

    private void handlePathByHttpStatus() {
        if (status == NOT_FOUND) {
            filePath = ERROR_PAGE;
        } else if (status == BAD_REQUEST) {
            filePath = ERROR_PAGE;
        } else if (status == FOUND) {
            filePath = MAIN_PAGE;
        }
    }

    public void writeResponseToOutputStream(OutputStream out){
        DataOutputStream dos = new DataOutputStream(out);
        ContentType type = ContentType.findBy(this.filePath);
        try {
            if (status == OK) {
                response200Header(dos, type);
            } else if (status == NOT_FOUND) {
                response400Header(dos, type);
            } else if (status == BAD_REQUEST) {
                response404Header(dos, type);
            } else if (status == FOUND) {
                response302Header(dos);
            }
            responseCookieHeader(dos);
            responseBody(dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseCookieHeader(DataOutputStream dos) throws IOException {
        for (Cookie cookie : cookies) {
            dos.writeBytes("Set-Cookie: " + cookie.toString() + "\r\n");
        }
    }

    private void response200Header(DataOutputStream dos, ContentType type) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes(String.format("Content-Type: %s;charset=utf-8\r\n", type.getMime()));
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
    }

    private void response302Header(DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 302 OK \r\n");
        dos.writeBytes("Location: " + INDEX + "\r\n");
    }

    private void response400Header(DataOutputStream dos, ContentType type) throws IOException {
        dos.writeBytes("HTTP/1.1 400 NOT_FOUND \r\n");
        dos.writeBytes(String.format("Content-Type: %s;charset=utf-8\r\n", type.getMime()));
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
    }

    private void response404Header(DataOutputStream dos, ContentType type) throws IOException {
        dos.writeBytes("HTTP/1.1 404 BAD_REQUEST \r\n");
        dos.writeBytes(String.format("Content-Type: %s;charset=utf-8\r\n", type.getMime()));
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
    }

    private void responseBody(DataOutputStream dos) throws IOException {
        dos.writeBytes("\r\n");
        dos.write(body, 0, body.length);
        dos.flush();
    }

    public void addCookie(String name, String value) {
        cookies.add(Cookie.from(name, value));
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "status=" + status +
                ", filePath='" + filePath + '\'' +
                ", cookies=" + cookies +
                '}';
    }
}
