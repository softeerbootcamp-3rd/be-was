package util;

import annotation.NotEmpty;
import constant.ParamType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<String, String> parseQueryString(String queryString) throws UnsupportedEncodingException {
        if (queryString == null || queryString.isEmpty())
            return new HashMap<>();
        queryString = decodeUri(queryString);
        return extractKeyValue(queryString, "&", "=");
    }

    public static String decodeUri(String encodedUrl) throws UnsupportedEncodingException {
        return URLDecoder.decode(encodedUrl, "UTF-8");
    }

    public static <T> T mapToClass(Map<String, String> mapInfo, Class<T> destClass)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        T result = destClass.getDeclaredConstructor().newInstance();
        for (Field field : destClass.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldValue = mapInfo.get(field.getName());
            NotEmpty notEmptyAnnotation = field.getDeclaredAnnotation(NotEmpty.class);
            if (notEmptyAnnotation != null && (fieldValue == null || fieldValue.isEmpty()))
                throw new IllegalArgumentException("Field '" + field.getName() + "' cannot be null");
            if (fieldValue != null) {
                ParamType paramType = ParamType.getByClass(field.getType());
                field.set(result, paramType.map(fieldValue));
            }
        }
        return result;
    }

    public static Map<String, String> parseCookie(String cookieString) {
        if (cookieString == null || cookieString.isEmpty())
            return new HashMap<>();
        return extractKeyValue(cookieString, "; ", "=");
    }

    private static Map<String, String> extractKeyValue(String input, String delimiter1, String delimiter2) {
        Map<String, String> result = new HashMap<>();
        String[] entries = input.split(delimiter1);
        for (String entry : entries) {
            String[] parts = entry.split(delimiter2);
            if (parts.length == 2) {
                result.put(parts[0], parts[1]);
            }
        }
        return result;
    }
}
