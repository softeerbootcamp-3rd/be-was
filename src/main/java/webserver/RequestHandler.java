package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import controller.FrontController;
import model.HttpMethod;
import model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        HashMap<String, String> paramMap = getParamMap(query);
        httpRequest.setParamMap(paramMap);
    }

    private void setRequestBody(HttpRequest httpRequest, BufferedReader br) throws IOException {
        Map<String, String> headerMap = httpRequest.getHeaderMap();
        StringBuilder sb = new StringBuilder();

        int contentLength = Integer.parseInt(headerMap.get("Content-Length"));
        char[] buffer = new char[BUFFER_SIZE];
        int bytesRead;
        while (contentLength > 0 && (bytesRead = br.read(buffer, 0, Math.min(contentLength, BUFFER_SIZE))) != -1) {
            sb.append(buffer, 0, bytesRead);
            contentLength -= bytesRead;
        }

        if (headerMap.get("Content-Type").startsWith("application/x-www-form-urlencoded")) {
            setRequestParamFromQuery(httpRequest, sb.toString());
        }

        httpRequest.setBody(sb.toString());
    }

    private void setRequestHeaderMap(HttpRequest httpRequest, BufferedReader br) throws IOException {
        Map<String, String> headerMap = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            // localhost:{port}에도 ':'이 들어가기 때문에 setParamMap()과 로직 다름
            int idx = line.indexOf(":");
            headerMap.put(line.substring(0, idx), line.substring(idx + 2)); // +2 -> ':' 다음에 나오는 공백을 제거해주기 위함
        }

        httpRequest.setHeaderMap(headerMap);
    }

    private void setRequestLine(HttpRequest httpRequest, String status) throws UnsupportedEncodingException {
        String[] tokens = status.split(" ");

        if (tokens[0].equals(HttpMethod.GET.name())) {
            httpRequest.setMethod(HttpMethod.GET);
        }
        if (tokens[0].equals(HttpMethod.POST.name())) {
            httpRequest.setMethod(HttpMethod.POST);
        }

        String uri = tokens[1];
        if (uri.contains("?")) {
            String[] uriWithQuery = uri.split("\\?");
            httpRequest.setURI(uriWithQuery[0]);
            setRequestParamFromQuery(httpRequest, uriWithQuery[1]);
        } else {
            httpRequest.setURI(uri);
        }

        httpRequest.setHttpVer(tokens[2]);
    }

    private HashMap<String, String> getParamMap(String query) throws UnsupportedEncodingException {
        HashMap<String, String> paramMap = new HashMap<>();

        query = URLDecoder.decode(query, "UTF-8");
        String[] params = query.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            paramMap.put(getKey(keyValue), getValue(keyValue));
        }

        return paramMap;
    }

    private static String getValue(String[] keyValue) {
        return keyValue[1].trim();
    }

    private static String getKey(String[] keyValue) {
        return keyValue[0];
    }

}

