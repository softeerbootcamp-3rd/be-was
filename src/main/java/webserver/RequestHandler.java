package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.http.*;
import model.http.request.HttpRequest;
import model.http.request.RequestHeaders;
import model.http.request.StartLine;
import model.http.response.HttpResponse;
import model.http.response.ResponseHeaders;
import model.http.response.StatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    public static final String TEMPLATES_RESOURCE = "src/main/resources/templates";
    public static final String STATIC_RESOURCES = "src/main/resources/static";
    public static final String HTTP_VERSION = "HTTP/1.1";

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader inBufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            HttpRequest httpRequest = getRequestHeader(inBufferedReader);
            logger.debug(httpRequest.toString());
            byte[] body = Files.readAllBytes(getFilePath(httpRequest));
            setHttpResponse(httpRequest, out, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    private void setHttpResponse(HttpRequest httpRequest, OutputStream out, byte[] body) {
        StatusLine statusLine = setHeaderStatusOK();
        ResponseHeaders responseHeaders = setResponseHeaders(httpRequest, body.length);
        Body responseBody = setBody(body);
        HttpResponse httpResponse = new HttpResponse(statusLine, responseHeaders, responseBody);
        DataOutputStream dos = new DataOutputStream(out);
        setResponseStatusAndHeader(dos, httpResponse);
        setResponseBody(dos, httpResponse);
    }

    private void setResponseBody(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.write(httpResponse.getBody().getContent(), 0, httpResponse.getBody().getContent().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void setResponseStatusAndHeader(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.getStatusLine().getStatusHeader());
            dos.writeBytes(httpResponse.getHeaders().getContentTypeHeader());
            dos.writeBytes(httpResponse.getHeaders().getContentLengthHeader());
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Body setBody(byte[] body) {
        return new Body(body);
    }

    private ResponseHeaders setResponseHeaders(HttpRequest httpRequest, int length) {
        if(httpRequest.getHeaders().getAccept().contains("css")){
            return new ResponseHeaders(ContentType.CSS, length);
        }
        if(httpRequest.getHeaders().getAccept().contains("js")){
            return new ResponseHeaders(ContentType.JAVASCRIPT, length);
        }
        if(httpRequest.getHeaders().getAccept().contains("html")){
            return new ResponseHeaders(ContentType.HTML, length);
        }
        return new ResponseHeaders(ContentType.MIME, length);
    }

    private StatusLine setHeaderStatusOK() {
        return new StatusLine(HTTP_VERSION, Status.OK);
    }

    private static Path getFilePath(HttpRequest header) {
        String filePath = header.getStartLine().getPathUrl();
        if(filePath.equals("/")){
            filePath += "index.html";
        }
        if (filePath.contains("html")) {
            return new File(TEMPLATES_RESOURCE + filePath).toPath();
        }
        return new File(STATIC_RESOURCES + filePath).toPath();

    }

    private HttpRequest getRequestHeader(BufferedReader inBufferedReader) throws IOException {
        List<String> httpRequest = new ArrayList<>();
        String temp;
        while (inBufferedReader != null && !(temp = inBufferedReader.readLine()).isEmpty()){
            httpRequest.add(temp);
        }
        StartLine startLine = parseStartLine(httpRequest);
        RequestHeaders requestHeaders = parseRequestHeaders(httpRequest);
        Body body = parseRequestBody(httpRequest);
        return new HttpRequest(startLine, requestHeaders, body);
    }

    private Body parseRequestBody(List<String> httpRequest) {
        return null;
    }

    private RequestHeaders parseRequestHeaders(List<String> httpRequest) {
        HashMap<String, String> header = parseHeaderFields(httpRequest);

        String host = header.remove("Host");
        String userAgent = header.remove("User-Agent");
        String accept = header.remove("Accept");

        return new RequestHeaders(host, userAgent, accept, header);
    }

    private HashMap<String, String> parseHeaderFields(List<String> httpRequest) {
        HashMap<String, String> header = new HashMap<>();

        for (int i = 1; i < httpRequest.size(); i++) {
            String[] strings = httpRequest.get(i).split(": ");
            header.put(strings[0], strings[1]);
        }

        return header;
    }

    private StartLine parseStartLine(List<String> content) {
        String[] startLine = content.get(0).split(" ");
        return new StartLine(HttpMethod.valueOf(startLine[0]),startLine[1], startLine[2]);
    }
}
