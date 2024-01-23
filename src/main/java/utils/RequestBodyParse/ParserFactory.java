package utils.RequestBodyParse;

import webserver.http.ContentType;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParserFactory {
    public interface ContentParser {
        Map<String, String> parse(List<String> body);
    }

    public static class JsonParser implements ContentParser {
        @Override
        public Map<String, String> parse(List<String> body) {
            return Arrays.stream(body.get(0).split("&"))
                    .map(it -> it.split("=", 2))
                    .collect(Collectors.toMap(
                            it -> it[0],
                            it -> URLDecoder.decode(it.length > 1 ? it[1] : "", StandardCharsets.UTF_8),
                            (existing, replacement) -> existing
                    ));
        }
    }

    public static class FormUrlEncodedParser implements ContentParser {
        @Override
        public Map<String, String> parse(List<String> body) {
            return Arrays.stream(body.get(0).split("&"))
                    .map(it -> it.split("=", 2))
                    .collect(Collectors.toMap(
                            it -> it[0],
                            it -> URLDecoder.decode(it.length > 1 ? it[1] : "", StandardCharsets.UTF_8),
                            (existing, replacement) -> existing
                    ));
        }
    }


    public static ContentParser getParser(ContentType contentType) {
        switch (contentType) {
            case APPLICATION_JSON:
                return new JsonParser();
            case APPLICATION_X_WWW_FORM_URLENCODED:
                return new FormUrlEncodedParser();
            default:
                throw new IllegalArgumentException("Unsupported content type");
        }
    }


}
