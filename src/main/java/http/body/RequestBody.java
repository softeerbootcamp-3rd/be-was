package http.body;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class RequestBody {
    private String rawString;

    private RequestBody(String rawString) {
        this.rawString = rawString;
    }

    public static RequestBody createEmptyBody() {
        return new RequestBody("");
    }

    public static RequestBody createFromReader(BufferedReader reader, int contentLength) throws IOException {
        char[] bodyRawString = new char[contentLength];

        reader.read(bodyRawString, 0, contentLength);

        return new RequestBody(String.valueOf(bodyRawString));
    }

    public Map<String, String> convertRawStringAsMap() throws IOException {
        Map<String, String> result = new HashMap<>();
        String[] propertyString = rawString.split("&");

        for (String property : propertyString) {
            String[] property_split = property.split("=");
            if (property_split.length != 2 || property_split[1].isBlank())
                throw new IOException("Incorrect Query String");

            property_split[1] = URLDecoder.decode(property_split[1], "UTF-8");
            result.put(property_split[0], property_split[1]);
        }

        return result;
    }
}
