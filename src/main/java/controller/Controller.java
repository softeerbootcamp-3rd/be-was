package controller;

import request.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

public interface Controller {
    public abstract void route(HttpRequest httpRequest, OutputStream out) throws IOException; // 요청 URL에 따라 Controller에서 처리
}
