package controller;

import request.HttpRequest;
import util.StatusCode;

public interface Controller {
    StatusCode route(HttpRequest httpRequest); // 요청 URL에 따라 Controller에서 처리
}
