package dto.request;

import config.Config;
import dto.response.HTTPResponseDto;

import java.util.Arrays;

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
    CREATEPOST("/qna/form.html") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return Config.httpPostService.createPost(httpRequestDto);
        }
    },
    ERROR("wrong request") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());
        }
    };

    private String url;

    PostRequestEnum(String url) {
        this.url = url;
    }

    // 요청 url에 해당하는 상수 반환
    public static PostRequestEnum getRequest(String url) {
        return Arrays.stream(PostRequestEnum.values())
                .filter(request -> request.url.equals(url))
                .findAny()
                .orElse(ERROR);
    }

    // 상수별로 상속받을 함수
    public abstract HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto);
}
