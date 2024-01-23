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
    private Map<String, String> body = new HashMap<>();

    public HttpRequest(BufferedReader br) throws IOException {
        // method, url, version 파싱
        String line = br.readLine();
        String[] tokens;

        requestLine = new RequestLine(Parser.splitRequestList(line));
        generalHeader = new GeneralHeader();

        while (!(line = br.readLine()).isEmpty()) {
            tokens = line.split(": ", 2);
            String key = tokens[0], value = tokens[1];
            if (generalHeader.checkGeneralHeader(key)) {
                generalHeader.addGeneralHeader(key, value);
            } else {
                etcHeaders.put(key, value);
            }
        }

        // body부 받기
        if (etcHeaders.containsKey("Content-Length")) {
            // Content-Length의 길이가 페이로드의 길이
            // 해당 길이만큼 남은 데이터 읽어오기
            int len = Integer.parseInt(etcHeaders.get("Content-Length"));
            char[] buffer = new char[len];
            int bytesRead = br.read(buffer, 0, len);

            line = new String(buffer, 0, bytesRead);
            body = Parser.extractParams(line);
        }
    }

    @Override
    public String toString() {
        return "\n======================HttpRequest======================" +
                "\n요청 URI: " + requestLine.getUri() +
                "\n요청 메서드: " + requestLine.getMethod() +
                "\nHttp 버전: " + requestLine.getVersion() + "\n" +
                "\n- General Header\n" + mapToString(generalHeader.getGeneralHeaders()) +
                "\n- 기타 헤더\n" + mapToString(etcHeaders) +
                (!body.isEmpty()? "\n- 바디(페이로드)\n" + mapToString(body) : null);
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

    public Map<String, String> getEtcHeaders() {
        return etcHeaders;
    }
}
