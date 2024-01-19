package controller;

import model.Response;

public interface Controller {

    Response route(String url);
}
