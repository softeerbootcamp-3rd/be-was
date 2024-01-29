package webserver.http;

import util.StringParser;
import webserver.MultipartFile;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class HttpRequest {

    private String ip;
    private String port;
    private String method;
    private String path;
    private String protocol;
    private HttpHeader requestHeader;
    private Map<String, String> params;
    private Map<String, String> body;
    private List<MultipartFile> files;

    public HttpRequest(Socket socket, InputStream in) throws IndexOutOfBoundsException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        // Read the request line
        String requestLine = br.readLine();
        String[] requestLineParts = requestLine.split(" ");
        this.ip = socket.getInetAddress().toString();
        this.port = String.valueOf(socket.getPort());
        this.method = requestLineParts[0];
        this.path = requestLineParts[1];
        this.protocol = requestLineParts[2];

        Map<String, List<String>> headers = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(":");
            String headerName = headerParts[0];
            String headerValue = headerParts[1].trim();
            headers.computeIfAbsent(headerName, key -> List.of(headerValue));
        }

        this.requestHeader = new HttpHeader(headers);
        this.params = StringParser.parseQueryString(requestLineParts[1]);

//        if (requestHeader.isMultipart()) {
//            // 멀티파트 요청인 경우 파일 파싱
//            this.files = new ArrayList<>();
//            this.body = new HashMap<>();
//            System.out.println("aaaaa");
//            parseMultipartRequest(br, requestHeader.getBoundary());
//            System.out.println("bbbbb");
//        } else if (requestHeader.getContentLength() != null) {
//            // 멀티파트가 아니면서 Content-Length가 있는 경우
//            Integer bodySize = Integer.parseInt(requestHeader.getContentLength());
//            char[] bodyArr = new char[bodySize];
//            String bodyStr = "";
//            if ((br.read(bodyArr, 0, bodySize)) != -1) {
//                bodyStr = new String(bodyArr, 0, bodySize);
//            }
//            Map<String, String> bodyMap = StringParser.parseKeyValue(bodyStr);
//            this.body = bodyMap;
//        }

        if (requestHeader.getContentLength() != null) {
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

    private void parseMultipartRequest(BufferedReader br, String boundary) throws IOException {
//        String line;
//
//        while ((line = br.readLine()) != null) {
//            if (line.equals("--" + boundary)) {
//                // 파트 시작을 나타내는 boundary를 찾았습니다.
//
//                // Content-Disposition 헤더를 찾기 위해 빈 라인을 스킵합니다.
//                while ((line = br.readLine()) != null && !line.isEmpty()) {
//                    // Content-Disposition 헤더를 찾았습니다.
//                    //System.out.println("line = " + line);
//                    if (line.startsWith("Content-Disposition:")) {
//                        // Content-Disposition 헤더에서 name 속성을 추출합니다.
//                        String name = extractNameFromContentDisposition(line);
//
//                        // 파일 여부를 확인합니다.
//                        if (line.contains("filename")) {
//                            // 파일인 경우, 파일 정보를 읽어와서 저장합니다.
//                            handleFilePart(br, name);
//                        } else {
//                            // 파일이 아닌 경우, 단순한 텍스트 파트로 처리할 수 있습니다.
//                            handleTextPart(br, name);
//                        }
//                    }
//                }
//
//                // 수정: 현재 파트가 마지막 파트인 경우 루프를 빠져나갑니다.
//                if (line.equals("--" + boundary + "--")) {
//                    break;
//                }
//            }
//        }

        Integer bodySize = Integer.parseInt(requestHeader.getContentLength());
        char[] bodyArr = new char[bodySize];
        String bodyStr = "";
        if ((br.read(bodyArr, 0, bodySize)) != -1) {
            bodyStr = new String(bodyArr, 0, bodySize);
        }
        parseMultipartString(bodyStr);
    }

    private void parseMultipartString(String multipartString) {
        String[] parts = multipartString.split("\n------");

        for (String part : parts) {
            if (!part.trim().isEmpty()) {
                if (part.contains("Content-Disposition")) {
                    String name = extractNameFromContentDisposition(part);
                    if (part.contains("filename")) {
                        // 파일 파트
                        String filename = extractFilenameFromContentDisposition(part);
                        String contentType = extractContentType(part);
                        byte[] fileContent = extractFileContent(part);
                        handleFilePart(name, filename, contentType, fileContent);
                    } else {
                        // 텍스트 파트
                        String textContent = extractTextContent(part);
                        handleTextPart(name, textContent);
                    }
                }
            }
        }
    }

    private static String extractNameFromContentDisposition(String part) {
        String[] lines = part.split("\n");
        for (String line : lines) {
            if (line.startsWith("Content-Disposition")) {
                int startIndex = line.indexOf("name=\"") + 6;
                int endIndex = line.indexOf("\"", startIndex);
                return line.substring(startIndex, endIndex);
            }
        }
        return null;
    }

    private static String extractFilenameFromContentDisposition(String part) {
        String[] lines = part.split("\n");
        for (String line : lines) {
            if (line.startsWith("Content-Disposition") && line.contains("filename")) {
                int startIndex = line.indexOf("filename=\"") + 10;
                int endIndex = line.indexOf("\"", startIndex);
                return line.substring(startIndex, endIndex);
            }
        }
        return null;
    }

    private String extractContentType(String part) {
        String[] lines = part.split("\n");
        for (String line : lines) {
            if (line.startsWith("Content-Type")) {
                return line.substring("Content-Type: ".length());
            }
        }
        return null;
    }

    private String extractTextContent(String part) {
        String[] lines = part.split("\n");
        StringBuilder textContent = new StringBuilder();
        boolean isReadingText = false;
        for (String line : lines) {
            if (isReadingText) {
                if (line.startsWith("------")) {
                    break;
                }
                textContent.append(line).append("\n");
            }
            if (line.trim().isEmpty()) {
                isReadingText = true;
            }
        }
        return textContent.toString().trim();
    }

    private byte[] extractFileContent(String part) {
        // Content-Disposition 헤더에서 filename을 찾습니다.
        int filenameIndex = part.indexOf("filename=\"");
        if (filenameIndex == -1) {
            // filename이 없다면 파일 데이터가 없는 것으로 간주합니다.
            return new byte[0];
        }

        // filename의 시작 위치를 찾습니다.
        int startQuoteIndex = filenameIndex + "filename=\"".length();

        // filename의 끝 위치를 찾습니다.
        int endQuoteIndex = part.indexOf("\"", startQuoteIndex);
        if (endQuoteIndex == -1) {
            // 따옴표가 누락된 경우 예외 처리 또는 오류 처리를 수행할 수 있습니다.
            return new byte[0];
        }

        // filename을 추출합니다.
        String filename = part.substring(startQuoteIndex, endQuoteIndex);

        // 파일 데이터의 시작 위치를 찾습니다.
        int dataStartIndex = part.indexOf("\r\n\r\n") + "\r\n\r\n".length();

        // 파일 데이터를 추출합니다.
        String fileDataString = part.substring(dataStartIndex);

        // Base64 디코딩을 수행하여 byte 배열로 변환합니다.
        return Base64.getDecoder().decode(fileDataString);
    }

    private void handleTextPart(String name, String textContent) {
        System.out.println(name + ", " + textContent);
        this.body.put(name, textContent);
    }

    private void handleFilePart(String name, String filename, String contentType, byte[] fileContent) {
        MultipartFile file = new MultipartFile(filename, contentType, fileContent);
        System.out.println(filename + ", " + contentType);
        this.files.add(file);
    }


    public String getHttpMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        if (params != null)
            return params;
        if (body != null)
            return body;
        return null;
    }

    public List<MultipartFile> getFiles() {
        return files != null ? files : new ArrayList<>();
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
