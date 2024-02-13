package dto.request;

import config.Config;
import dto.response.HTTPResponseDto;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum PostRequestEnum {
    SIGNUP("/user/create") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpPostService.signup(httpRequestDto);
        }
    },
    LOGIN("/user/login") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpPostService.login(httpRequestDto);
        }
    },
    CREATE_OR_UPDATE_POST("/qna/form\\.html(?:/(\\d+))?") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpPostService.createOrUpdatePost(httpRequestDto);
        }
    },
    ERROR("wrong request") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());
        }
    };

    private String urlPattern;

    PostRequestEnum(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    // 요청 url에 해당하는 상수 반환
    public static PostRequestEnum getRequest(String url) {
        return Arrays.stream(PostRequestEnum.values())
                .filter(request -> request.patternMatcher(url))
                .findAny()
                .orElse(ERROR);
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
