package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
