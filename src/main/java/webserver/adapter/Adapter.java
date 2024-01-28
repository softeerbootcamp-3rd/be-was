package webserver.adapter;

import webserver.request.Request;
import webserver.response.Response;

public interface Adapter {
    Response run(Request request) throws Throwable;
    boolean canRun(Request request);
}
