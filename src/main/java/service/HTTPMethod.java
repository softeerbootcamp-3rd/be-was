package service;

import model.RequestHeader;

import java.io.OutputStream;

public interface HTTPMethod {
    void process(String path, RequestHeader requestHeader);

    String getContentType();

    String getDetailPath();
}
