package dto;

import java.util.Arrays;
import java.util.HashMap;

public enum ResponseEnum {

    OK(200) {
        @Override
        public String getStatusline() {
            return STATUSLINE200;
        }
        @Override
        public HashMap<String, String> addConnection2Header(HashMap<String, String> header) {
            header.put("Connection", "keep-alive");
            return header;
        }
    },
    REDIRECTION(302) {
        @Override
        public String getStatusline() {
            return STATUSLINE302;
        }
        @Override
        public HashMap<String, String> addConnection2Header(HashMap<String, String> header) {
            header.put("Connection", "close");
            return header;
        }
    },
    BAD_REQUEST(400) {
        @Override
        public String getStatusline() {
            return STATUSLINE400;
        }
        @Override
        public HashMap<String, String> addConnection2Header(HashMap<String, String> header) {
            header.put("Connection", "keep-alive");
            return header;
        }
    },
    SERVER_ERROR(500) {
        @Override
        public String getStatusline() {
            return STATUSLINE500;
        }
        @Override
        public HashMap<String, String> addConnection2Header(HashMap<String, String> header) {
            header.put("Connection", "close");
            return header;
        }
    };

    private int statusCode;

    private static final String STATUSLINE200 = "HTTP/1.1 200 OK\r\n";
    private static final String STATUSLINE302 = "HTTP/1.1 302 Found\r\n";
    private static final String STATUSLINE400 = "HTTP/1.1 400 Bad Request\r\n";
    private static final String STATUSLINE500 = "HTTP/1.1 500 Server Error\r\n";

    ResponseEnum(int statusCode) {
        this.statusCode = statusCode;
    }

    // status code에 해당하는 상수 반환
    public static ResponseEnum getResponse(int statusCode) {
        return Arrays.stream(ResponseEnum.values())
                .filter(response -> response.statusCode == statusCode)
                .findAny()
                .orElse(SERVER_ERROR);
    }

    // 상수별 status line 반환
    public abstract String getStatusline();

    // 상수별 header에 connection 추가
    public abstract HashMap<String, String> addConnection2Header(HashMap<String, String> header);

}
