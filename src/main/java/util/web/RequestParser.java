package util.web;

import constant.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.mapper.MultipartMapper;
import util.mapper.ObjectMapper;
import webserver.HttpRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestParser {

    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public static String extractPath(String url) {
        try {
            URI uri = new URI(url);
            return uri.getPath();
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static String extractQuery(String url) {
        try {
            URI uri = new URI(url);
            return uri.getQuery();
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static String decodeUri(String encodedUrl) throws UnsupportedEncodingException {
        return URLDecoder.decode(encodedUrl, "UTF-8");
    }

    public static Map<String, String> parseQueryString(String queryString) throws UnsupportedEncodingException {
        if (queryString == null || queryString.isEmpty())
            return new HashMap<>();
        queryString = decodeUri(queryString);
        return extractKeyValue(queryString, "&");
    }

    public static Map<String, String> parseCookie(String cookieString) {
        if (cookieString == null || cookieString.isEmpty())
            return new HashMap<>();
        return extractKeyValue(cookieString, "; ");
    }

    private static Map<String, String> extractKeyValue(String input, String delimiter1) {
        Map<String, String> result = new HashMap<>();
        if (input == null)
            return result;
        String[] entries = input.split(delimiter1);
        for (String entry : entries) {
            String[] parts = entry.split("=");
            if (parts.length == 2) {
                result.put(parts[0], parts[1]);
            }
        }
        return result;
    }

    public static <T> T parseBody(HttpRequest request, Class<T> clazz)
            throws IOException, InvocationTargetException,
            IllegalAccessException, NoSuchMethodException, InstantiationException {

        Map<String, String> queryMap;
        String contentType = request.getHeader().get(HttpHeader.CONTENT_TYPE);
        String[] entries = new String[1];
        entries[0] = contentType;

        if (contentType != null)
            entries = contentType.split("; ");

        if (Objects.equals(entries[0], "application/x-www-form-urlencoded")) {
            queryMap = parseQueryString(new String(request.getBody()));
            return ObjectMapper.mapToClass(queryMap, clazz);
        } else if (Objects.equals(entries[0], "multipart/form-data")) {
            return MultipartMapper.mapMultipartFile(entries, clazz);
        }
        return ObjectMapper.jsonToClass(new String(request.getBody()), clazz);
    }
}
