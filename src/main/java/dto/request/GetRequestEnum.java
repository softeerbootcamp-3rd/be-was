package dto.request;

import config.Config;
import dto.response.HTTPResponseDto;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    USERLIST("/user/list\\.html") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpGetService.showWithLogin(httpRequestDto);
        }
    },
    PROFILE("/user/profile\\.html(?:/[a-zA-Z0-9가-힣]+)?") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            System.out.println("here!!!");
            return Config.httpGetService.showWithLogin(httpRequestDto);
        }
    },
    WRITEPOST("/qna/form\\.html") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpGetService.showWithLogin(httpRequestDto);
        }
    },
    UPDATEPOST("/qna/form\\.html/(\\d+)") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpGetService.updatePost(httpRequestDto);
        }
    },
    SHOWPOST("/qna/show\\.html/(\\d+)") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpGetService.showWithLogin(httpRequestDto);
        }
    },
    DELETEPOST("/qna/(\\d+)/delete") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpGetService.deletePost(httpRequestDto);
        }
    },
    FILE("file") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpGetService.requestFile(httpRequestDto);
        }
    };

    private String urlPattern;

    GetRequestEnum(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    // 요청 url에 해당하는 상수 반환
    public static GetRequestEnum getRequest(String url) {
        return Arrays.stream(GetRequestEnum.values())
                .filter(request -> request.patternMatcher(url))
                .findAny()
                .orElse(FILE);
    }

    // 상수별로 상속받을 함수
    public abstract HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto);

    // 정규표현식을 이용한 패턴 매칭 결과 반환
    private boolean patternMatcher(String url) {
        Pattern urlPattern = Pattern.compile(this.urlPattern);
        Matcher matcher = urlPattern.matcher(url);
        return matcher.matches();
    }

}
