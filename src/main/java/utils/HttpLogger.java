package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class HttpLogger {

    private static final Logger logger = LoggerFactory.getLogger(HttpLogger.class);

    public static void requestLogging(HttpRequest httpRequest){
        StringBuilder logMessage = new StringBuilder();

        logMessage.append("\n========== HTTP Request ==========\n");
        logMessage.append("Method: ").append(httpRequest.getMethod()).append("\n");
        logMessage.append("Path: ").append(httpRequest.getPath()).append("\n");
        if (httpRequest.getQueryParams() != null) {
            httpRequest.getQueryParams().forEach((key, value) ->
                    logMessage.append("QueryParam: ").append(key).append(" = ").append(value).append("\n"));
        }
        logMessage.append("Protocol Version: ").append(httpRequest.getProtocolVersion()).append("\n");
        if (httpRequest.getHeaders() != null) {
            httpRequest.getHeaders().forEach((key, value) ->
                    logMessage.append("Header: ").append(key).append(" = ").append(value).append("\n"));
        }
        if (!httpRequest.getBody().isEmpty()) {
            httpRequest.getBody().forEach((key, value) ->
                    logMessage.append("Body: ").append(key).append(" = ").append(value).append("\n"));
        }
        logMessage.append("==================================");

        logger.debug(logMessage.toString());
    }

    public static void responseLogging(HttpResponse httpResponse){
        StringBuilder logMessage = new StringBuilder();

        logMessage.append("\n========== HTTP Response ==========\n");
        logMessage.append("HTTP Version: ").append(httpResponse.getHttpVersion()).append("\n");
        logMessage.append("Status: ").append(httpResponse.getStatusCode()).append(" ").append(httpResponse.getStatusText()).append("\n");
        if (httpResponse.getHeaders() != null) {
            httpResponse.getHeaders().forEach((key, value) ->
                    logMessage.append("Header: ").append(key).append(" = ").append(value).append("\n"));
        }
        logMessage.append("==================================");

        logger.debug(logMessage.toString());
    }
}
