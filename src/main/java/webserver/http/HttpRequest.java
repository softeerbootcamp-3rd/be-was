package webserver.http;

import file.MultipartFile;
import util.StringParser;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class HttpRequest {

    private String ip;
    private String port;
    private String method;
    private String path;
    private String protocol;
    private String boundary;
    private HttpHeader requestHeader;
    private Map<String, String> params;
    private Map<String, String> body;
    private byte[] byteBody;

    public HttpRequest(Socket socket, InputStream in) throws IndexOutOfBoundsException, IOException {

        String requestLine = readLine(in);
        String[] requestLineParts = requestLine.split(" ");
        this.ip = socket.getInetAddress().toString();
        this.port = String.valueOf(socket.getPort());
        this.method = requestLineParts[0];
        this.path = requestLineParts[1];
        this.protocol = requestLineParts[2];

        Map<String, List<String>> headers = new HashMap<>();
        String line;
        while ((line = readLine(in)) != null && !line.isEmpty()) {
            String[] headerParts = line.split(":");
            String headerName = headerParts[0];
            String headerValue = headerParts[1].trim();

            if (headerName.equals("Content-Type") && headerValue.contains("multipart/form-data")) {
                String[] multipartArray = headerValue.split(";");
                headerValue = multipartArray[0];
                this.boundary = multipartArray[1].split("=")[1];
            }

            String finalHeaderValue = headerValue;
            headers.computeIfAbsent(headerName, key -> List.of(finalHeaderValue));
        }

        this.requestHeader = new HttpHeader(headers);
        this.params = StringParser.parseQueryString(requestLineParts[1]);

        if (requestHeader.getContentLength() != null && requestHeader.getContentType().equals("multipart/form-data")) {
            int contentLength = Integer.parseInt(requestHeader.getContentLength());
            this.byteBody = new byte[contentLength];
            in.read(byteBody);

        } else if (requestHeader.getContentLength() != null && !requestHeader.getContentType().equals("multipart/form-data")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Integer bodySize = Integer.parseInt(requestHeader.getContentLength());
            char[] bodyArr = new char[bodySize];
            String bodyStr = "";
            if ((br.read(bodyArr, 0, bodySize)) != -1) {
                bodyStr = new String(bodyArr, 0, bodySize);
            }
            Map<String, String> bodyMap = StringParser.parseKeyValue(bodyStr);
            this.body = bodyMap;
        }

    }

    private static String readLine(InputStream inputStream) throws IOException {
        //TODO: byte[]로 받아서 바로 String 변환
        StringBuilder lineBuffer = new StringBuilder();
        int currentChar;

        while ((currentChar = inputStream.read()) != -1) {
            char charRead = (char) currentChar;
            lineBuffer.append(charRead);

            if (charRead == '\n') {
                break;
            }
        }

        return lineBuffer.toString().trim();
    }

    public String getHttpMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getBoundary() {
        return boundary;
    }

    public byte[] getByteBody() {
        return byteBody;
    }

    public Map<String, String> getParams() {
        if (params != null)
            return params;
        if (body != null)
            return body;
        return null;
    }


    public String getCookie() {
        return requestHeader.getCookie();
    }

    @Override
    public String toString() {
        return "Request [ip=" + ip + ", port=" + port
                + ", method=" + method + ", path=" + path
                + ", http_version=" + protocol + ", host=" + requestHeader.getHost()
                + ", accept=" + requestHeader.getAccept() + ", cookie=" + getCookie() + "]";
    }


}
