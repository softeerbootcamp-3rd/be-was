package Controller.User;

import Controller.User.UserLogic.UserLogic;
import HTTPModel.Request;
import HTTPModel.Response;
import SessionManager.SessionManager;
import model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class UserController {

    private SessionManager sessionManager = SessionManager.getInstance();
    private String URI;
    private Request request;

    private static Map<String, String> URLMapping;

    static {
        URLMapping = new HashMap<>();
        URLMapping.put("form.html", "userForm");
        URLMapping.put("loginPost", "userLoginPost");
        URLMapping.put("create", "userSignup");
        URLMapping.put("login.html", "userLogin");
        URLMapping.put("list", "userList");
        URLMapping.put("profile.html", "userProfile");
    }

    //private Session Session = new Session();

    private static final Logger logger = LoggerFactory.getLogger(webserver.RequestHandler.class);
    public UserController(String URI, Request request)
    {
        this.URI = URI;
        this.request = request;

    }

    public Response UserLogic(Request request) {

        Response response = new Response();
        String sid = request.GetSid();
        User logined_user = null;

        if(sid != null){
            //logined_user = Session.getSession(sid);
            logined_user = sessionManager.getSession().getSession(sid);
        }

        try {

            String functionName = URLMapping.get(URI);
            Class<?> userLogicClass = UserLogic.class;
            Method method = userLogicClass.getDeclaredMethod(functionName, Request.class, User.class);
            method.setAccessible(true);
            UserLogic userLogicInstance = new UserLogic();

            response = (Response) method.invoke(userLogicInstance ,request, logined_user);

            /*

            if ("form.html".equals(URI)) {

                response = UserLogic.userForm(request, logined_user);

            }else if("loginPost".equals(URI)){

                response = UserLogic.userLoginPost(request,logined_user);

            }else if(URI.startsWith("create")){

                response = UserLogic.userSignup(request, logined_user);


            }else if("login.html".equals(URI)){

                response = UserLogic.userLogin(request, logined_user);


            }else if("list".equals(URI)){

                response = UserLogic.userList(request, logined_user);

            }else if("profile.html".equals(URI)){

                response = UserLogic.userProfile(request, logined_user);

            }

             */

        }catch(Exception e){

        }
        return response;
    }

}
