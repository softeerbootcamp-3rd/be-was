package header;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.StringTokenizer;

public class RequestHeader {
    private static final Logger logger = LoggerFactory.getLogger(RequestHeader.class);

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

    public static RequestHeader of(String method, String path, String Host){
        return new RequestHeader(method, path, Host);
    }

    public static RequestHeader parseHeader(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        StringTokenizer st = new StringTokenizer(requestLine, " ");

        String method = st.nextToken();
        String path = st.nextToken();
        String protocol = st.nextToken();

        RequestHeader requestHeader = of(method, path, protocol);

        while(!(requestLine = br.readLine()).isEmpty()){
            String[] requestLineSplit = requestLine.split(": ");

            String headerName = requestLineSplit[0].replace("-", "");
            String headerValue = requestLineSplit[1];

            setHeaderValue(requestHeader, headerName, headerValue);
        }

        return requestHeader;
    }

    private static void setHeaderValue(RequestHeader requestHeader, String fieldName, String fieldValue){
        Class<RequestHeader> requestHeaderClass = RequestHeader.class;

        try{
            Field field = requestHeaderClass.getDeclaredField(fieldName);
            field.setAccessible(true);

            field.set(requestHeader, fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
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