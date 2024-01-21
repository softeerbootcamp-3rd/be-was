package webserver;

import dto.RequestHeaderDto;
import dto.RequestLineDto;
import common.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    public static RequestLineDto parseRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        String[] methodAndURL = Util.splitRequestLineToMethodAndURL(line);
        if (isQueryStringExist(methodAndURL[1])) {
            String[] pathAndQueryString = Util.splitUrlToPathAndQueryString(methodAndURL[1]);
            return new RequestLineDto(methodAndURL[0], pathAndQueryString[0], pathAndQueryString[1]);
        }
        return new RequestLineDto(methodAndURL[0], methodAndURL[1]);
    }

    public static RequestHeaderDto parseRequestHeader(BufferedReader br) throws IOException {
        Map<RequestHeader, String> requestHeaders = new HashMap<>();

        String line = br.readLine();
        while (!line.equals("")) {
            String[] requestHeader = Util.splitRequestHeader(line);
            RequestHeader property = RequestHeader.findProperty(requestHeader[0]);
            if (property != RequestHeader.NONE) {
                requestHeaders.put(property, requestHeader[1]);
            }
            line = br.readLine();
        }

        return new RequestHeaderDto(requestHeaders);
    }

    public static Map<String, String> parseParameters(String query) {
        Map<String, String> parameters = new HashMap<>();
        for (String parameter : Util.splitParamters(query)) {
            String[] tokens = Util.splitParameter(parameter);
            parameters.put(tokens[0], tokens[1]);
        }
        return parameters;
    }

    private static boolean isQueryStringExist(String url) {
        return url.contains("?");
    }
}
