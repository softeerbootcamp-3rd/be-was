package controller;

import request.HttpRequest;
import util.StatusCode;

public interface Controller {
    StatusCode handleUserRequest(HttpRequest httpRequest);
}
