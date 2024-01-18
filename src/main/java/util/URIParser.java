package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class URIParser {

    private static final Logger logger = LoggerFactory.getLogger(URIParser.class);

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

    public static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> paramMap = new HashMap<>();

        if (queryString != null && !queryString.isEmpty()) {
            queryString = decodeUrl(queryString);
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

    public static String decodeUrl(String encodedUrl) {
        return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
    }
}
