package webserver.http;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.UserFormDataParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Map<Route, Consumer<Request>> routeHandlers= new HashMap<>();

    private static class Route{
        private final HttpMethod httpMethod;
        private final String routeName;

        private Route(HttpMethod httpMethod, String routeName){
            this.httpMethod = httpMethod;
            this.routeName = routeName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Route route = (Route) o;
            return httpMethod == route.httpMethod &&
                    Objects.equals(routeName, route.routeName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(httpMethod, routeName);
        }
    }

    public RequestHandler() {
        initializeRoutes();
    }

    private void initializeRoutes() {
        routeHandlers.put(new Route(HttpMethod.GET,"/user/create"), (Request r)-> getUserCreate(r));
        routeHandlers.put(new Route(HttpMethod.POST,"/user/create"), (Request r)-> postUserCreate(r));
    }

    private void postUserCreate(Request r) {
    }

    public void handleRequest(Request request) {
        String requestTarget = request.getRequestTarget().split("\\?")[0];
        Route inputRoute = new Route(request.getHttpMethod() ,requestTarget);
        if (routeHandlers.containsKey(inputRoute)) {
            routeHandlers.get(inputRoute).accept(request);
        } else {
            handleNotFound();
        }
    }

    private void getUserCreate(Request request) {
        String data = request.getRequestTarget().split("\\?")[1];
        UserFormDataParser userFormDataParser = new UserFormDataParser(data);
        HashMap<String,String> formData = new HashMap<>(userFormDataParser.parseData());
        User user = new User(formData.get("userId"), formData.get("password"), formData.get("name"), formData.get("email") );
        System.out.println(user.toString());
        //리다이렉션 헤더에 넣기
        request.addRequestHeader("Location","/user/form.html");
    }

    private void handleNotFound() {
        logger.error("request : NOT FOUND");
    }
}