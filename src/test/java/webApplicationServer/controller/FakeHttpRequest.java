package webApplicationServer.controller;

import model.http.Body;
import model.http.HttpMethod;
import model.http.request.HttpRequest;
import model.http.request.RequestHeaders;
import model.http.request.StartLine;

import java.util.HashMap;

public class FakeHttpRequest extends HttpRequest {

    public FakeHttpRequest() {
        super(new StartLine(HttpMethod.GET, "", ""),
                new RequestHeaders("host", "userAgent", "*/*", new HashMap<>()),
                new Body("".getBytes()));
    }

    public FakeHttpRequest(String fakePath) {
        super(new StartLine(HttpMethod.GET, fakePath, ""),
                new RequestHeaders("host", "userAgent", "*/*", new HashMap<>()),
                new Body("".getBytes()));
    }
    public FakeHttpRequest(String fakePath, byte[] body) {
        super(new StartLine(HttpMethod.GET, fakePath, ""),
                new RequestHeaders("host", "userAgent", "*/*", new HashMap<>()),
                new Body(body));
    }

}
