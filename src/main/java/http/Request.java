package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ViewResolver;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.net.URLDecoder;
import java.util.Optional;

public class Request {
    //상수 및 클래스 변수
    private static final Logger logger = LoggerFactory.getLogger(Request.class);
    //인스턴스 변수
    private String method;
    private String url;
    private String location = "";
    private Map<String,String> requestParam = new HashMap<>();
    private ArrayList<Cookie> cookies = new ArrayList<>();
    private Map<String,String> body = new HashMap<>();

    public Map<String, String> getBody() {
        return body;
    }

    public void setBody(String bodyString) {
        String[] fields = bodyString.split("&");
        for(String field : fields){
            String[] data = field.split("=");
            String key = field.split("=")[0];
            String value = (data.length == 2) ? decodeValue(data[1]) : "";
            this.body.put(key, value);
        }
    }

    private String decodeValue(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // UTF-8 인코딩이 지원되지 않을 경우의 예외 처리
            e.printStackTrace();
            return value;
        }
    }
    //생성자
    public Request(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        String firstLine = readLine(bufferedInputStream);
        this.method = firstLine.split(" ")[0];
        this.setUrl(firstLine.split(" ")[1]);

        Map<String, String> headers = readHeaders(bufferedInputStream);

        if(headers.containsKey("Content-Length")){
            int contentLength = Integer.parseInt(headers.get("Content-Length"));

            // 본문 읽기
            byte[] buffer = new byte[contentLength];
            int bytesRead;
            int totalBytesRead = 0;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            while (totalBytesRead < contentLength && (bytesRead = bufferedInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
            }

            String bodyString = byteArrayOutputStream.toString("UTF-8");
            logger.debug("bodyString = {}",bodyString);
            this.setBody(bodyString);
        }

    }

    public Request(String method, String url, String sid,String body) throws UnsupportedEncodingException {
        this.method = method;
        this.setUrl(url);
        this.setCookies(sid);
        this.setBody(body);
    }
    //메서드
    public String getUrl() {
        return url;
    }
    public String getLocation() {return location;}
    public Map<String, String> getRequestParam() {return requestParam;}

    public String getMethod() {return method;}

    public void setMethod(String method) {this.method = method;}
    public void setRequestParam(String[] params) throws UnsupportedEncodingException {
        for (String param : params) {
            String[] data = param.split("\\=");
            if(data.length!=2){
                this.requestParam.put(data[0], "");
            }
            else{
                this.requestParam.put(data[0], URLDecoder.decode(data[1],"UTF-8"));
            }
        }
    }

    public void setUrl(String string) throws UnsupportedEncodingException {
        String[] request = string.split("\\?");
        url = request[0];
        if(ViewResolver.isTemplate(url)||ViewResolver.isStatic(url)){
            requestParam.put("content",url);
        }
        if(request.length>1) {
            setRequestParam(request[1].split("\\&"));
        }
    }

    private String readLine(InputStream inputStream) throws IOException {
        StringBuilder line = new StringBuilder();
        int data;
        while ((data = inputStream.read()) != -1) {
            char currentChar = (char) data;
            line.append(currentChar);
            if (currentChar == '\n') {
                break;
            }
        }
        return line.toString().trim();
    }

    private Map<String, String> readHeaders(InputStream inputStream) throws IOException {
        Map<String, String> headers = new HashMap<>();

        String line;
        while (!(line = readLine(inputStream)).isEmpty()) {
            String[] headerParts = line.split(":");
            if (headerParts.length == 2) {
                String headerName = headerParts[0].trim();
                String headerValue = headerParts[1].trim();

                headers.put(headerName, headerValue);
            }
        }
        if(headers.containsKey("Cookie")) {
            setCookies(headers.get("Cookie"));
        }
        return headers;
    }

    public ArrayList<Cookie> getCookies() {
        return cookies.isEmpty() ? null : cookies;
    }
    public void setCookies(String cookieString){
        String[] cookiePairs = cookieString.split("; ");
        for (String cookiePair : cookiePairs) {
            String[] keyValue = cookiePair.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                cookies.add(new Cookie(key, value));
            }
        }
    }

}
