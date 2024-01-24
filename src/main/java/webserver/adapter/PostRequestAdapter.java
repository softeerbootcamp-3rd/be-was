package webserver.adapter;

import webserver.annotation.PostMapping;
import webserver.exception.GeneralException;
import webserver.request.Request;
import webserver.response.Response;
import webserver.status.ErrorCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PostRequestAdapter extends MethodRequestAdapter{
    private static final PostRequestAdapter postRequestAdapter = new PostRequestAdapter();

    private PostRequestAdapter(){}

    public static PostRequestAdapter getInstance(){
        return postRequestAdapter;
    }

    @Override
    public Response run(Request request) throws Throwable {
        Method method = findMethod(PostMapping.class, request.getPath());

        if(method == null){
            throw new GeneralException(ErrorCode.RESOURCE_NOT_FOUND_ERROR);
        }

        Object[] params = createParams(method, request);

        try {
            Object o = executeMethod(method, params);

            if(o instanceof Response){
                return (Response) o;
            }

            return null;
        } catch (InvocationTargetException e){
            throw e.getTargetException();
        }
    }

    @Override
    public boolean canRun(Request request) {
        return request.getMethod().equals("POST");
    }
}
