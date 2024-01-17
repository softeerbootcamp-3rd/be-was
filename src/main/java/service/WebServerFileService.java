package service;

import model.http.request.HttpRequest;

public interface WebServerFileService {
    byte[] getFile(HttpRequest httpRequest);
}