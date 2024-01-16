package handler;

import controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.GetRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class GetRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetRequestHandler.class);

    public static void map(GetRequest getRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(getRequest.getPath().equals("/user/create")) {
            UserController userController = new UserController();
            Class<UserController> userControllerClass = UserController.class;

            Method method = userControllerClass.getDeclaredMethod("createUser", String.class, String.class, String.class, String.class);
            Parameter[] parameters = method.getParameters();

            Object[] params = new Object[parameters.length];
            int index = 0;

            for(Parameter parameter: parameters){
                for(String parameterName: getRequest.getParamsMap().keySet()){
                    if(parameterName.equals(parameter.getName())){
                        params[index++] = getRequest.getParamsMap().get(parameterName);

                        break;
                    }
                }
            }

            method.invoke(userController, params);
        } else{
            throw new NoSuchMethodException("요청 PATH가 존재하지 않음");
        }
    }
}
