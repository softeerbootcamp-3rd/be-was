package utils.RequestBodyParse;

import webserver.http.ContentType;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ParserFactory {
    public interface ContentParser {
        Map<String, String> parse(char[] body);
    }

    public static class JsonParser implements ContentParser {
        @Override
        public Map<String, String> parse(char[] body) {
            return null;
        }
    }

    public static class FormUrlEncodedParser implements ContentParser {
        @Override
        public Map<String, String> parse(char[] body) {
            String bodyAsString = new String(body);
            return Arrays.stream(bodyAsString.split("&"))
                    .map(it -> it.split("=", 2))
                    .collect(Collectors.toMap(
                            it -> it[0],
                            it -> {
                                try {
                                    return URLDecoder.decode(it.length > 1 ? it[1] : "", "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    throw new RuntimeException(e);
                                }
                            },
                            (existing, replacement) -> existing
                    ));
        }
    }


    public static ContentParser getParser(ContentType contentType) {
        if (contentType == ContentType.APPLICATION_JSON) {
            return new JsonParser();
        } else if (contentType == ContentType.APPLICATION_X_WWW_FORM_URLENCODED) {
            return new FormUrlEncodedParser();
        } else {
            throw new IllegalArgumentException("Unsupported content type");
        }
    }

}
