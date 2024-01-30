package webserver.http;


import db.PostRepository;
import db.SessionManager;
import db.UserRepository;
import db.dto.CreatePost;
import db.dto.GetPost;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.LoginChecker;
import utils.UserFormDataParser;
import webserver.http.constants.HttpMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import static java.lang.Integer.parseInt;

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
        routeHandlers.put(new Route(HttpMethod.POST,"/user/login"), (Request r)-> postUserLogin(r));
        routeHandlers.put(new Route(HttpMethod.GET,"/user/list"), (Request r)-> getUserList(r));
        routeHandlers.put(new Route(HttpMethod.GET,"/user/list.html"), (Request r)-> getUserList(r));
        routeHandlers.put(new Route(HttpMethod.GET,"/post/form.html"), (Request r)-> getBoardWrite(r));
        routeHandlers.put(new Route(HttpMethod.POST,"/post/create"), (Request r)-> postPostCreate(r));
        routeHandlers.put(new Route(HttpMethod.GET,"/post/show.html"), (Request r)-> getPostShow(r));
        routeHandlers.put(new Route(HttpMethod.GET,"/user/logout.html"), (Request r)-> getUserLogout(r));

    }

    public void handleRequest(Request request) {
        if (request.getHttpMethod() == HttpMethod.NULL)
            throw new IllegalArgumentException("Method NULL");
        String requestTarget = request.getRequestTarget().split("\\?")[0];
        Route inputRoute = new Route(request.getHttpMethod() ,requestTarget);
        if (routeHandlers.containsKey(inputRoute)) {
            routeHandlers.get(inputRoute).accept(request);
        } else {
            handleNotFound(request);
        }
    }

    private void getUserCreate(Request request) {
        String data = request.getRequestTarget().split("\\?")[1];
        UserFormDataParser userFormDataParser = new UserFormDataParser(data);
        HashMap<String,String> formData = new HashMap<>(userFormDataParser.parseData());
        User user = new User(formData.get("userId"), formData.get("password"), formData.get("name"), formData.get("email") );
        UserRepository.adduser(user);
        request.addRequestHeader("Location","/user/form.html");
    }

    private void postUserCreate(Request request) {
        HashMap<String,String> formData = (HashMap<String, String>) request.getRequestBody();
        User user = new User(formData.get("userId"), formData.get("password"), formData.get("name"), formData.get("email") );

        //같은 아이디 회원 가입 방지
        if(UserRepository.findUserById(user.getUserId()) != null) {
            request.addRequestHeader("Location","/index.html");
            return;
        }
        UserRepository.adduser(user);
        request.addRequestHeader("Location","/index.html");
    }

    private void postUserLogin(Request request) {
        HashMap<String,String> formData = (HashMap<String, String>) request.getRequestBody();
        String id = formData.get("userId");
        String pw = formData.get("password");
        if(UserRepository.isValidLogin(id, pw)){
            request.addRequestHeader("Location","/index.html");
            String session = SessionManager.addSession(UserRepository.findUserById(id));
            request.addRequestHeader("Set-Cookie", "sid=" + session + "; Path=/");
        }else{
            request.addRequestHeader("Location","/user/login_failed.html");
        }
    }

    private void getUserList(Request request) {
        if(!LoginChecker.loginCheck(request)){
            request.addRequestHeader("Location","/user/login.html");
        }
    }

    private void getBoardWrite(Request request) {
        if(!LoginChecker.loginCheck(request)){
            request.addRequestHeader("Location","/user/login.html");
        }
    }

    private void postPostCreate(Request request) {
        if(!LoginChecker.loginCheck(request)){
            request.addRequestHeader("Location","/user/login.html");
        }

        HashMap<String,String> formData = (HashMap<String, String>) request.getRequestBody();
        CreatePost post = new CreatePost(formData.get("writer"), formData.get("title"), formData.get("contents") );
        PostRepository.addPost(post);
        request.addRequestHeader("Location","/index.html");
    }

    private void getPostShow(Request request) {
        if(!LoginChecker.loginCheck(request)){
            request.addRequestHeader("Location","/user/login.html");
        }

        String data = request.getRequestTarget().split("\\?")[1].split("=")[1];
        GetPost getPost = PostRepository.findById(parseInt(data));
        HashMap<String, String> requestedData = new HashMap<>();

        requestedData.put("id",String.valueOf(getPost.getId()));
        requestedData.put("writer", getPost.getWriter());
        requestedData.put("title", getPost.getTitle());
        requestedData.put("content", getPost.getContent());
        requestedData.put("createdtime", getPost.getCreatedTime().toString());
        requestedData.put("commentcount", String.valueOf(getPost.getCommentCount()));

        request.setRequestedData(requestedData);
    }



    private void getUserLogout(Request request) {
        SessionManager.printAllUsers();
        SessionManager.deleteSession(request.getRequestHeader().get("Cookie").split("=")[1]);
        request.addRequestHeader("Location","/index.html");
    }


    private void handleNotFound(Request request) {
        //405에러처리
        String requestTarget = request.getRequestTarget().split("\\?")[0]; // 쿼리 스트링 제거
        boolean routeExists = routeHandlers.keySet().stream()
                .anyMatch(route -> route.routeName.equals(requestTarget));
        if(routeExists){
            request.addRequestHeader("Error","405");
        }
    }
}