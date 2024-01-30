package http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private final RequestLine requestLine;
    private final GeneralHeader generalHeader;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();
    private MultiPartForm multiPartForm;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        // method, url, version 파싱
        String line = br.readLine();

        requestLine = new RequestLine(Parser.splitRequestList(line));
        generalHeader = new GeneralHeader();

        parseHeaders(br);

        logger.info(requestLine.toString());

        // GET - uri에서 params 분리
        if (requestLine.getUri().contains("?")) {
            String query = Parser.extractQuery(requestLine.getUri());
            String decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
            params = Parser.extractParams(decodedQuery);
        }

        // Content-Type이 multipart/form-data인 경우 MultiPartForm 생성
        String contentType = getContentType();
        if (contentType != null && contentType.startsWith("multipart/form-data")) {
            logger.debug("멀티파트 폼 데이터 처리");
            multiPartForm = new MultiPartForm(br, contentType);
        } else if (headers.containsKey("Content-Length")) {
            // Content-Length의 길이가 페이로드의 길이
            // 해당 길이만큼 남은 데이터 읽어오기
            int len = Integer.parseInt(headers.get("Content-Length"));
            char[] buffer = new char[len];
            int bytesRead = br.read(buffer, 0, len);

            line = new String(buffer, 0, bytesRead);
            logger.info(line);
            String decodedLine = URLDecoder.decode(line, StandardCharsets.UTF_8);
            params = Parser.extractParams(decodedLine);
        }
    }

    private void parseHeaders(BufferedReader br) throws IOException {
        String line;
        String[] tokens;

        while (!(line = br.readLine()).isEmpty()) {
            tokens = line.split(": ", 2);
            String key = tokens[0], value = tokens[1];
            if (generalHeader.checkGeneralHeader(key)) {
                generalHeader.addGeneralHeader(key, value);
            } else {
                headers.put(key, value);
            }
        }
    }

    public String getEtcHeaderValue(String key) {
        return this.headers.getOrDefault(key, "");

    }

    public String getCookie(String key) {
        return this.headers.getOrDefault("Cookie", "").contains(key) ?
                Parser.extractCookieValue(this.headers.get("Cookie"), key) : "";
    }

    public String getContentType() {
        return this.headers.getOrDefault("Content-Type", "");
    }

    public GeneralHeader getGeneralHeader() {
        return generalHeader;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public MultiPartForm getMultiPartForm() {
        return multiPartForm;
    }

    public String mapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((key, value) -> sb.append(key).append(": ").append(value).append("\n"));

        return sb.toString();
    }

    @Override
    public String toString() {
        return "\n======================HttpRequest======================" +
                "\n요청 URI: " + requestLine.getUri() +
                "\n요청 메서드: " + requestLine.getMethod() +
                "\nHttp 버전: " + requestLine.getVersion() + "\n" +
                "\n- General Header\n" + mapToString(generalHeader.getGeneralHeaders()) +
                "\n- 기타 헤더\n" + mapToString(headers) +
                (!params.isEmpty()? "\n- uri 쿼리 파라미터\n" + mapToString(params) : "") +
                (!params.isEmpty()? "\n- 바디(페이로드)\n" + mapToString(params) : "") +
                (multiPartForm != null ? "\n- 멀티파트 폼 데이터\n" + mapToString(multiPartForm.getFields()) + "\n" : "");
    }
}
