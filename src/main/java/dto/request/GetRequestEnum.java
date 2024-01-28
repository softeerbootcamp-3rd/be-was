package dto.request;

import config.Config;
import dto.response.HTTPResponseDto;

import java.io.IOException;
import java.util.Arrays;

public enum GetRequestEnum {
    DEFAULT("/") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return new HTTPResponseDto("/index.html");
        }
    },
    SIGNUP("/user/create") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpGetService.signup(httpRequestDto);
        }
    },
    LOGOUT("/logout") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpGetService.logout(httpRequestDto);
        }
    },
    USERLIST("/user/list.html") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpGetService.showWithLogin(httpRequestDto);
        }
    },
    PROFILE("/user/profile.html") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpGetService.showWithLogin(httpRequestDto);
        }
    },
    FILE("file") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpGetService.requestFile(httpRequestDto);
        }
    };

    private String url;

    GetRequestEnum(String url) {
        this.url = url;
    }

    // 요청 url에 해당하는 상수 반환
    public static GetRequestEnum getRequest(String url) {
        return Arrays.stream(GetRequestEnum.values())
                .filter(request -> request.url.equals(url))
                .findAny()
                .orElse(FILE);
    }

    // 상수별로 상속받을 함수
    public abstract HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto);

}
