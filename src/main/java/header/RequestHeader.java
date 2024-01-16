package header;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.StringTokenizer;

public class RequestHeader {
    private String method;
    private String path;
    private String protocol;
    private String Host;
    private String UserAgent;
    private String Connection;
    private String SecFetchSite;
    private String SecFetchMode;
    private String SecFetchDest;
    private String Accept;
    private String AcceptEncoding;
    private String AcceptLanguage;
    private String Referer;
    private String UpgradeInsecureRequests;

    private RequestHeader(String method, String path, String protocol){
        this.method = method;
        this.path = path;
        this.protocol = protocol;
    }

    public String getPath(){
        return path;
    }

    public String getMethod(){
        return method;
    }

    public static RequestHeader of(String method, String path, String Host){
        return new RequestHeader(method, path, Host);
    }

    @Override
    public String toString(){
        return String.format("\n요청 메소드: %s\n" +
                "요청한 자원의 경로: %s\n" +
                "프로토콜: %s\n" +
                "서버의 주소: %s\n" +
                "콘텐츠 타입: %s\n" +
                "사용자의 Agent: %s\n" +
                "연결 설정: %s\n" +
                "언어: %s\n" +
                "이전 경로: %s\n", method, path, protocol, Host, Accept, UserAgent, Connection, AcceptLanguage, Referer);
    }
}