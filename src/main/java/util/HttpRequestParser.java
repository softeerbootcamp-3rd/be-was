package util;

import model.http.Body;
import model.http.HttpMethod;
import model.http.request.HttpRequest;
import model.http.request.RequestHeaders;
import model.http.request.StartLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpRequestParser {

    public Body parseRequestBody(List<String> httpRequest) {
        return null;
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
        return new StartLine(HttpMethod.valueOf(startLine[0]),startLine[1], startLine[2]);
    }
}