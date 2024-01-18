package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String url;
    private String version;
    private String host;
    private String userAgent;
    private String accept;
    private String acceptLanguage;
    private String connection;
    private String referer;
    private Map<String, String> etc = new HashMap<>();

    public HttpRequest(BufferedReader br) throws IOException {
        // method, url, version 파싱
        String line = br.readLine();
        String[] tokens = Parser.parsing(line, " ", 3);

        this.method = tokens[0];
        this.url = tokens[1];
        this.version = tokens[2];

        while (!(line = br.readLine()).isEmpty()) {
            tokens = Parser.parsing(line, ": ", 2);
            switch (tokens[0]) {
                case "User-Agent":
                    this.userAgent = tokens[1];
                    break;

                case "Accept-Language":
                    this.acceptLanguage = tokens[1];
                    break;

                case "Accept":
                    this.accept = tokens[1];
                    break;

                case "Referer":
                    this.referer = tokens[1];
                    break;

                case "Connection":
                    this.connection = tokens[1];
                    break;

                case "Host":
                    this.host = tokens[1];
                    break;

                default:
                    this.etc.put(tokens[0], tokens[1]);
            }
        }
    }

    @Override
    public String toString() {
        return "===========HttpRequest===========" +
                "\n요청 URL: " + url +
                "\n요청 메서드: " + method +
                "\nHttp 버전: " + version + "\n" +
                "\n- 요청 헤더" +
                "\n호스트: " + host +
                "\n사용자 에이전트: " + userAgent + "\n" +
                "\n받아들일 수 있는 파일 형식: " + accept +
                "\n해석 가능한 언어: " + acceptLanguage +
                "\n참조: " + referer +
                "\n연결: " + connection;
//                + "\n- 기타 헤더"
//                + "\n" + mapToString();
    }


    public String mapToString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : etc.entrySet()) {
            sb.append(entry.getKey())
                    .append(" = ")
                    .append(entry.getValue())
                    .append("\n");
        }

        return sb.toString();
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public String getReferer() {
        return referer;
    }

    public String getAccept() {
        return accept;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public String getUserAgent() {
        return userAgent;
    }
}