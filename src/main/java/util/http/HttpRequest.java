package util.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private RequestEntity<?> request;

    private HttpRequest() {}

    public static HttpRequest parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        HttpRequest httpRequest = new HttpRequest();

        String line = br.readLine();
        String[] tokens = line.split(" ");
        HttpMethod method = HttpMethod.valueOf(tokens[0]);
        URI url = URI.create(tokens[1]);

        String accept = MediaType.ALL_VALUE;

        logger.debug(line);
        while (!(line = br.readLine()).isEmpty()) {
            tokens = line.split(" ");
            if (tokens[0].equals("Accept:")) {
                accept = tokens[1].split(",")[0];
                logger.debug(line);
            }
            if (tokens[0].equals("Host:") || tokens[0].equals("Connection:"))
                logger.debug(line);
        }

        httpRequest.request = RequestEntity.method(method, url)
                .header(HttpHeaders.ACCEPT, accept)
                .build();

        return httpRequest;
    }

    public String getPath() {
        return this.request.getUrl().getPath();
    }

    public String getHeader(String headerName) {
        return this.request.getHeaders().getFirst(headerName);
    }

    public Map<String, String> getQueryMap() {
        Map<String, String> queryMap = new HashMap<>();

        String query = this.request.getUrl().getQuery();
        String[] params = query.split("&");

        for (String s : params) {
            String[] keyVal = s.split("=");
            queryMap.put(keyVal[0], keyVal[1]);
        }
        return queryMap;
    }
}
