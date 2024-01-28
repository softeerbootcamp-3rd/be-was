package dto.response;

import java.util.Arrays;
import java.util.HashMap;

public enum ResponseEnum {

    OK(200) {
        @Override
        public String getStatusLine() {
            return "HTTP/1.1 200 OK\r\n";
        }

        @Override
        public String getConnection() {
            return "keep-alive";
        }
    },
    REDIRECTION(302) {
        @Override
        public String getStatusLine() {
            return "HTTP/1.1 302 Found\r\n";
        }

        @Override
        public String getConnection() {
            return "close";
        }
    },
    BAD_REQUEST(400) {
        @Override
        public String getStatusLine() {
            return "HTTP/1.1 400 Bad Request\r\n";
        }

        @Override
        public String getConnection() {
            return "keep-alive";
        }
    },
    SERVER_ERROR(500) {
        @Override
        public String getStatusLine() {
            return "HTTP/1.1 500 Server Error\r\n";
        }

        @Override
        public String getConnection() {
            return "close";
        }
    };

    private int statusCode;

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
    public abstract String getStatusLine();
    // 상수별 connection 반환
    public abstract String getConnection();

}
