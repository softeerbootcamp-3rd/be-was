package webserver.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotation.GetMapping;
import webserver.exception.GeneralException;
import webserver.request.Request;
import webserver.response.Response;
import webserver.status.ErrorCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GetRequestAdapter extends MethodRequestAdapter{
    private static final Logger logger = LoggerFactory.getLogger(GetRequestAdapter.class);
    private static final GetRequestAdapter getRequestAdapter = new GetRequestAdapter();

    private GetRequestAdapter(){}

    public static GetRequestAdapter getInstance(){
        return getRequestAdapter;
    }

    @Override
    public Response run(Request request) throws Throwable {
        Method method = findMethod(GetMapping.class, request.getPath());

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
        return request.getMethod().equals("GET");
    }
}
