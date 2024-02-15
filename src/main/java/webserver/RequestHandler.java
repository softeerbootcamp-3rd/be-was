package webserver;

import constant.HttpMethod;
import controller.FrontController;
import model.HttpHeader;
import model.HttpRequest;
import model.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.URIParser;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final FrontController frontController = new FrontController();
    private static final int BUFFER_SIZE = 4096;

    private Socket connection;


    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = convertToRequest(in);
            logger.debug("request = {}", httpRequest);

            frontController.service(httpRequest, out);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpRequest convertToRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        HttpRequest httpRequest = new HttpRequest();

        // Request Line (method, path(query 포함), http version)
        setRequestLine(httpRequest, br.readLine());

        // Request Header
        setRequestHeaderMap(httpRequest, br);

        // Request Body (POST의 경우)
        if (httpRequest.getMethod() == HttpMethod.POST) {
            setRequestBody(httpRequest, br);
        }

        return httpRequest;
    }

    private void setRequestParamFromQuery(HttpRequest httpRequest, String query) throws UnsupportedEncodingException {
        Parameter paramMap = URIParser.getParamMap(query);
        httpRequest.setParamMap(paramMap);
    }

    private void setRequestBody(HttpRequest httpRequest, BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();

        int contentLength = Integer.parseInt(httpRequest.getHeader("Content-Length"));
        char[] buffer = new char[BUFFER_SIZE];
        int bytesRead;
        while (contentLength > 0 && (bytesRead = br.read(buffer, 0, Math.min(contentLength, BUFFER_SIZE))) != -1) {
            sb.append(buffer, 0, bytesRead);
            contentLength -= bytesRead;
        }

        if (httpRequest.getHeader("Content-Type").startsWith("application/x-www-form-urlencoded")) {
            setRequestParamFromQuery(httpRequest, sb.toString());
        }

        httpRequest.setBody(sb.toString());
    }

    private void setRequestHeaderMap(HttpRequest httpRequest, BufferedReader br) throws IOException {
        HttpHeader headerMap = new HttpHeader();
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            // localhost:{port}에도 ':'이 들어가기 때문에 setParamMap()과 로직 다름
            int idx = line.indexOf(":");
            headerMap.put(line.substring(0, idx), line.substring(idx + 2)); // +2 -> ':' 다음에 나오는 공백을 제거해주기 위함
        }

        httpRequest.setHeaderMap(headerMap);
    }

    private void setRequestLine(HttpRequest httpRequest, String statusLine) throws UnsupportedEncodingException {
        String[] statuses = statusLine.split(" ");

        if (statuses[0].equals(HttpMethod.GET.name())) {
            httpRequest.setMethod(HttpMethod.GET);
        }
        if (statuses[0].equals(HttpMethod.POST.name())) {
            httpRequest.setMethod(HttpMethod.POST);
        }

        String uriWithQuery = statuses[1];
        if (URIParser.hasQuery(uriWithQuery)) {
            String uri = URIParser.getUri(uriWithQuery);
            httpRequest.setURI(uri);

            String query = URIParser.getQuery(uriWithQuery);
            setRequestParamFromQuery(httpRequest, query);
        } else {
            httpRequest.setURI(uriWithQuery);
        }

        httpRequest.setHttpVer(statuses[2]);
    }

}

