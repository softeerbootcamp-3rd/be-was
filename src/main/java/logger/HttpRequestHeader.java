package logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpRequestHeader {
    private Map<String, String> headers;

    private HttpRequestHeader(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public static HttpRequestHeader httpRequestHeaderBuilder(InputStream in) {
        HashMap<String, String> map = new LinkedHashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = bufferedReader.readLine();
            String[] firstHeader = line.split(" ");
            map.put("method", firstHeader[0]);
            map.put("path", firstHeader[1]);
            map.put("protocol", firstHeader[2]);
            line = bufferedReader.readLine();
            while(!line.isEmpty()) {
                map.put(line.split(": ")[0], line.split(": ")[1]);
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            CustomLogger.printError(e);
        }

        return new HttpRequestHeader(map);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getPath() {
        return headers.get("path");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, value) -> sb.append(key).append(" : ").append(value).append("\n"));
        return sb.toString();
    }
}
