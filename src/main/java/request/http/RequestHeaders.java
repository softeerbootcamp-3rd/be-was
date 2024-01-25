package request.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestHeaders {
    private final Map<String, String> requestHeaders = new HashMap<>();

    public void setRequestHeaders(String readLine, BufferedReader br) throws IOException {
        for (readLine = br.readLine(); !readLine.isEmpty(); readLine = br.readLine()) {
            String[] headerTokens = readLine.split(": ");
            requestHeaders.put(headerTokens[0], headerTokens[1]);
        }
    }

    public String getValue(String key) {
        return requestHeaders.get(key);
    }
}
