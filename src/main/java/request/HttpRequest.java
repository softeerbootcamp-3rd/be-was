package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final String method;
    private final String URI;
    private final String httpVersion;
    private final Map<String, String> headers = new HashMap<>();
    //헤더 정보 저장 & 테스트
    public HttpRequest(BufferedReader br) throws IOException {
        String readLine = br.readLine();
        String[] tokens = readLine.split(" ");

        //request line 저장
        this.method = tokens[0];
        this.URI = tokens[1];
        this.httpVersion = tokens[2];

        //헤더 정보 저장
        while (true) {
            readLine = br.readLine();
            if (readLine.isEmpty()) break;

            String[] headerTokens = readLine.split(": ");
            headers.put(headerTokens[0], headerTokens[1]);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getURI() {
        return URI;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getHttpRequst() {
        return method + " " + URI + " " + httpVersion;
    }
}
