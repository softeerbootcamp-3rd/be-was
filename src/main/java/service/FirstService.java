package service;

import httpmessage.Request.HttpRequest;
import httpmessage.Response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FirstService {
    private final Map<String, Service> controllerMap = new HashMap<>();
    public FirstService() {
        initServiceMapping();
    }
    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        final String requestPath =  httpRequest.getPath();
        Service service = controllerMap.get(requestPath);
        if(service == null){
            service = new HomeService();
        }

        service.process(httpRequest,httpResponse);


    }

    private void initServiceMapping() {
        controllerMap.put("/", new HomeService());
        controllerMap.put("/index.html", new HomeService());
        controllerMap.put("/user/create", new UserCreateService());
        controllerMap.put("/user/login", new UserLoginService());
    }

}
