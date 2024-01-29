package controller.user;

import controller.ModelView;
import model.HttpRequest;
import model.HttpResponse;

public interface UserController {
    ModelView process(HttpRequest httpRequest, HttpResponse httpResponse);
}
