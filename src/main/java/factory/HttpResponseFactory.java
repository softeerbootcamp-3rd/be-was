package factory;

import model.http.request.HttpRequest;
import model.http.response.HttpResponse;

public interface HttpResponseFactory {
    HttpResponse create(HttpRequest httpRequest, byte[] body);
}