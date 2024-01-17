package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import controller.FrontController;
import model.HTTP_METHOD;
import model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final FrontController frontController = new FrontController();
    static final int BUFFER_SIZE = 4096;

    private Socket connection;
    static final String DEFAULT_STATIC_PATH = "./src/main/resources/templates";


    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = convertToRequest(in);
            logger.debug("request = {}", request);

            frontController.service(request, out);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Request convertToRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        Request request = new Request();

        // Request Line (method, path(query 포함), http version)
        setRequestLine(request, br.readLine());

        // Request Header
        setRequestHeaderMap(request, br);

        // Request Body (POST의 경우)
        if (request.getMethod() == HTTP_METHOD.POST) {
            setRequestBody(request, br);
        }

        return request;
    }

    private void setRequestParamFromQuery(Request request, String query) throws UnsupportedEncodingException {
        HashMap<String, String> paramMap = getParamMap(query);
        request.setParamMap(paramMap);
    }

    private void setRequestBody(Request request, BufferedReader br) throws IOException {
        Map<String, String> headerMap = request.getHeaderMap();
        StringBuilder sb = new StringBuilder();

        int contentLength = Integer.parseInt(headerMap.get("Content-Length"));
        char[] buffer = new char[BUFFER_SIZE];
        int bytesRead;
        while (contentLength > 0 && (bytesRead = br.read(buffer, 0, Math.min(contentLength, BUFFER_SIZE))) != -1) {
            sb.append(buffer, 0, bytesRead);
            contentLength -= bytesRead;
        }

        request.setBody(sb.toString());
    }

    private void setRequestHeaderMap(Request request, BufferedReader br) throws IOException {
        Map<String, String> headerMap = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            // localhost:{port}에도 ':'이 들어가기 때문에 setParamMap()과 로직 다름
            int idx = line.indexOf(":");
            headerMap.put(line.substring(0, idx), line.substring(idx + 2)); // +2 -> ':' 다음에 나오는 공백을 제거해주기 위함
        }

        request.setHeaderMap(headerMap);
    }

    private void setRequestLine(Request request, String status) throws UnsupportedEncodingException {
        String[] tokens = status.split(" ");

        if (tokens[0].equals(HTTP_METHOD.GET.name())) {
            request.setMethod(HTTP_METHOD.GET);
        }
        if (tokens[0].equals(HTTP_METHOD.POST.name())) {
            request.setMethod(HTTP_METHOD.POST);
        }

        String uri = tokens[1];
        if (uri.contains("?")) {
            String[] uriWithQuery = uri.split("\\?");
            request.setURI(uriWithQuery[0]);
            setRequestParamFromQuery(request, uriWithQuery[1]);
        } else {
            request.setURI(uri);
        }

        request.setHttpVer(tokens[2]);
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

