package controller;

import dto.ResponseDto;

public interface Controller {

    ResponseDto route(String url);
}
