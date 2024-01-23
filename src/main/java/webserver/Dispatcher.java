package webserver;

import http.request.Request;
import http.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dispatcher {
    private final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private final HandlerMapping handlerMapping = new HandlerMapping();

    private Request request;
    private Response response;

    public Dispatcher(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public void dispatch() {
        logger.debug("request : {}", request.toString());
        handlerMapping.findController(request.getUrl()).service(request, response);
    }
}
