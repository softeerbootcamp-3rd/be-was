package controller;

import model.Request;
import model.Response;

public interface Controller {

    /**
     * url에 따라 적절한 처리를 하는 함수를 호출합니다.
     * @param request 요청 정보
     */
    void route(Request request, Response response);
}
