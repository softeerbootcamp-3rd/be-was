package factory;

import model.http.request.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;

public interface HttpRequestFactory {
    HttpRequest create(BufferedReader bufferedReader) throws IOException;
}
