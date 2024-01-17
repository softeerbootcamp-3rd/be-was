package dto;

import service.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public enum GetRequestEnum {
    SIGNUP("/user/create") {
        @Override
        public byte[] doRequest(HTTPRequestDto httpRequestDto, HashMap<String, String> requestParams) throws IOException {
            return Service.signup(httpRequestDto, requestParams);
        }
    },
    FILE("file") {
        @Override
        public byte[] doRequest(HTTPRequestDto httpRequestDto, HashMap<String, String> requestParams) throws IOException {
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

    public abstract byte[] doRequest(HTTPRequestDto httpRequestDto, HashMap<String, String> requestParams) throws IOException;

}
