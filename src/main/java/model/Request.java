package model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private final String method;
    private final String urn;
    private final String http;
    private final int port;
    private final Map<String, String> headers = new HashMap<>();

    public Request(InputStream in, int portNumber) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(inputStreamReader);
        String header = br.readLine();

        // general header
        String[] generalHeader = header.split(" ");
        method = generalHeader[0];
        urn = generalHeader[1];
        http = generalHeader[2];
        port = portNumber;

        // request header
        String line = br.readLine();
        while (!line.isEmpty()) {
            String[] requestHeader = line.split(": ");
            headers.put(requestHeader[0], requestHeader[1]);
            line = br.readLine();
        }
    }

    public String getMethod() {
        return method;
    }

    public int getPort() {
        return port;
    }

    public String getUrl() {
        return urn;
    }

    public String getHttp() {
        return http;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }
}
