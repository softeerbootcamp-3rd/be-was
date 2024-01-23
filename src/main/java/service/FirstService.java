package service;

import httpmessage.Request.HttpRequest;
import httpmessage.Response.HttpResponse;
import httpmessage.View.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FirstService {
    private final Map<String, Service> controllerMap = new HashMap<>();

    public FirstService() {
        initServiceMapping();
    }

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
        controllerMap.put("/user/cerate", new UserCreateService());
        //controllerMap.put("/login", new UserLoginService());
    }

}
