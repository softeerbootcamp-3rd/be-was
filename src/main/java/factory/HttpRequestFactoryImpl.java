package factory;

import model.http.Body;
import model.http.request.HttpRequest;
import model.http.request.RequestHeaders;
import model.http.request.StartLine;
import util.HttpRequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestFactoryImpl implements HttpRequestFactory{
    private final HttpRequestParser httpRequestParser;

    public HttpRequestFactoryImpl(HttpRequestParser httpRequestParser) {
        this.httpRequestParser = httpRequestParser;
    }
    @Override
    public HttpRequest create(BufferedReader bufferedReader) throws IOException {
        List<String> httpRequest = getRequest(bufferedReader);
        StartLine startLine = httpRequestParser.parseStartLine(httpRequest);
        RequestHeaders requestHeaders = httpRequestParser.parseRequestHeaders(httpRequest);
        Body body = httpRequestParser.parseRequestBody(httpRequest);
        return new HttpRequest(startLine, requestHeaders, body);
    }

    private static List<String> getRequest(BufferedReader bufferedReader) throws IOException {
        List<String> httpRequest = new ArrayList<>();
        String temp;
        while (bufferedReader != null && !(temp = bufferedReader.readLine()).isEmpty()) {
            httpRequest.add(temp);
        }
        return httpRequest;
    }
}