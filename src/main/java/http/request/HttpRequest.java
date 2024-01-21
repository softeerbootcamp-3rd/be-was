package http.request;

import utils.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final RequestLine requestLine;
    private final GeneralHeader generalHeader;
    private Map<String, String> etcHeaders = new HashMap<>();
    private Map<String, String> params = new HashMap<>();

    // TODO : POST 추가할 때 body 필드 추가하기~~

    public HttpRequest(BufferedReader br) throws IOException {
        // method, url, version 파싱
        String line = br.readLine();
        String[] tokens;

        requestLine = new RequestLine(Parser.splitRequestList(line));
        generalHeader = new GeneralHeader();

        while (!(line = br.readLine()).isEmpty()) {
            tokens = Parser.parsing(line, ": ", 2);
            String key = tokens[0], value = tokens[1];
            if (generalHeader.checkGeneralHeader(key)) {
                generalHeader.addGeneralHeader(key, value);
            } else {
                etcHeaders.put(key, value);
            }
        }
    }

    @Override
    public String toString() {
        return "===========HttpRequest===========" +
                "\n요청 URI: " + requestLine.getUri() +
                "\n요청 메서드: " + requestLine.getMethod() +
                "\nHttp 버전: " + requestLine.getVersion() + "\n" +
                "\n- General Header" +
                mapToString(generalHeader.getGeneralHeaders()) +
                "\n- 기타 헤더" +
                "\n" + mapToString(etcHeaders);
    }

    public String mapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((key, value) -> sb.append(key).append(": ").append(value).append("\n"));

        return sb.toString();
    }

    public GeneralHeader getGeneralHeader() {
        return generalHeader;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }
}
