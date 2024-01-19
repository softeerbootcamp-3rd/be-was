package controller;

import util.StatusCode;

public interface Controller {
    StatusCode route(String RequestLine); // 요청 URL에 따라 Controller에서 처리
}
