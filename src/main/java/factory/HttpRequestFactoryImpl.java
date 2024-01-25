package factory;

import model.http.Body;
import model.http.HttpMethod;
import model.http.request.HttpRequest;
import model.http.request.RequestHeaders;
import model.http.request.StartLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpRequestFactoryImpl implements HttpRequestFactory {
    public static HttpRequestFactory getInstance() {
        return HttpRequestFactoryHolder.INSTANCE;
    }

    private static class HttpRequestFactoryHolder {
        private static final HttpRequestFactory INSTANCE = new HttpRequestFactoryImpl();
    }

    public HttpRequestFactoryImpl() {
    }

    @Override
    public HttpRequest create(BufferedReader bufferedReader) throws IOException {
        List<String> httpRequest = getRequest(bufferedReader);
        StartLine startLine = parseStartLine(httpRequest);
        RequestHeaders requestHeaders = parseRequestHeaders(httpRequest);
        Body body = parseRequestBody(bufferedReader, requestHeaders);
        return new HttpRequest(startLine, requestHeaders, body);
    }

    private Body parseRequestBody(BufferedReader bufferedReader, RequestHeaders requestHeaders) throws IOException {
         int contentLength = 0;
        if(requestHeaders.getOptionHeaders().containsKey("Content-Length")){
            contentLength = Integer.parseInt(requestHeaders.getOptionHeaders().get("Content-Length"));
        }
        char[] body = new char[contentLength];
        bufferedReader.read(body);
        String temp = new String(body);
        if(contentLength == 0){
            return new Body(null);
        }
        else{
            return new Body(temp);
        }
    }
    private List<String> getRequest(BufferedReader bufferedReader) throws IOException {
        List<String> httpRequest = new ArrayList<>();
        String temp;
        // Header 부분을 파싱
        while (bufferedReader != null && !(temp = bufferedReader.readLine()).isEmpty()) {
            httpRequest.add(temp);
        }
        return httpRequest;
    }
    public RequestHeaders parseRequestHeaders(List<String> httpRequest) {
        HashMap<String, String> header = parseHeaderFields(httpRequest);

        String host = header.remove("Host");
        String userAgent = header.remove("User-Agent");
        String accept = header.remove("Accept");

        return new RequestHeaders(host, userAgent, accept, header);
    }

    public HashMap<String, String> parseHeaderFields(List<String> httpRequest) {
        HashMap<String, String> header = new HashMap<>();

        for (int i = 1; i < httpRequest.size(); i++) {
            String[] strings = httpRequest.get(i).split(": ");
            header.put(strings[0], strings[1]);
        }

        return header;
    }

    public StartLine parseStartLine(List<String> content) {
        String[] startLine = content.get(0).split(" ");
        return new StartLine(HttpMethod.valueOf(startLine[0]), startLine[1], startLine[2]);
    }
}
