package controller;

import model.Request;
import model.Response;

public interface Controller {

    /**
     * url에 따라 적절한 처리를 하는 함수를 호출합니다.
     * @param request 요청 정보
     * @return 요청 작업을 수행한 결과를 담은 객체
     */
    Response route(Request request);
}
