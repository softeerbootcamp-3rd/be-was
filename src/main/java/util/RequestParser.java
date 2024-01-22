package util;

import constant.ParamType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
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
        Map<String, String> paramMap = new HashMap<>();

        if (queryString != null && !queryString.isEmpty()) {
            queryString = decodeUri(queryString);
            String[] params = queryString.split("&");

            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    paramMap.put(key, value);
                }
            }
        }

        return paramMap;
    }

    public static String decodeUri(String encodedUrl) throws UnsupportedEncodingException {
        return URLDecoder.decode(encodedUrl, "UTF-8");
    }

    public static <T> T mapToClass(Map<String, String> userInfo, Class<T> destClass) {
        try {
            T result = destClass.getDeclaredConstructor().newInstance();

            for (Map.Entry<String, String> entry : userInfo.entrySet()) {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue();

                try {
                    Field field = destClass.getDeclaredField(fieldName);
                    field.setAccessible(true);

                    ParamType paramType = ParamType.getByClass(field.getType());
                    field.set(result, paramType.map(fieldValue));
                } catch (NoSuchFieldException ignored) {}
            }

            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
