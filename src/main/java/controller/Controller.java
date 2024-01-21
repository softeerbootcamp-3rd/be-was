package controller;

import util.StatusCode;

public interface Controller {
    StatusCode handleUserRequest(String requestLine);
}
