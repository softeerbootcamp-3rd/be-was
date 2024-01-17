package dto;

import service.Service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public enum GetRequestEnum {
    SIGNUP("/user/create") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) throws IOException {
            return Service.signup(httpRequestDto);
        }
    },
    FILE("file") {
        @Override
        public HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) throws IOException {
            return Service.requestFile(httpRequestDto);
        }
    };

    private String url;

    GetRequestEnum(String url) {
        this.url = url;
    }

    public static GetRequestEnum getRequest(String url) {
        return Arrays.stream(GetRequestEnum.values())
                .filter(request -> request.url.equals(url))
                .findFirst()
                .orElse(FILE);
    }

    public abstract HTTPResponseDto doRequest(HTTPRequestDto httpRequestDto) throws IOException;

}
