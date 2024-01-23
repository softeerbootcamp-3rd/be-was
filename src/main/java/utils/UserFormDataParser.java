package utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class UserFormDataParser {
    private String data;
    public UserFormDataParser(String data) {
        this.data = data;
    }

    public Map<String, String> parseData() {
        return Arrays.stream(data.split("&"))
                .map(it -> it.split("=", 2))
                .collect(Collectors.toMap(
                        it -> it[0],
                        it -> URLDecoder.decode(it.length > 1 ? it[1] : "", StandardCharsets.UTF_8),
                        (existing, replacement) -> existing
                ));
    }
}
