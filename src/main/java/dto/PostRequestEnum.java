package dto;

import service.PostService;

import java.util.Arrays;

public enum PostRequestEnum {
    SIGNUP("/user/create") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return PostService.signup(httpRequestDto);
        }
    },
    LOGIN("/user/login") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return PostService.login(httpRequestDto);
        }
    },
    ERROR("wrong request") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) {
            return new HTTPResponseDto(404, "Bad Request".getBytes());
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
                .findFirst()
                .orElse(ERROR);
    }

    // 상수별로 상속받을 함수
    public abstract HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto);
}
