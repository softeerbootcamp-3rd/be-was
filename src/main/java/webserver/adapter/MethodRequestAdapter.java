package webserver.adapter;

import webserver.annotation.GetMapping;
import webserver.annotation.PostMapping;
import webserver.annotation.RequestBody;
import webserver.annotation.RequestParam;
import webserver.registry.ControllerRegistry;
import webserver.request.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public abstract class MethodRequestAdapter implements Adapter{
    protected Object executeMethod(Method method, Object[] params) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> c = method.getDeclaringClass();
        Object instance = c.getDeclaredConstructor().newInstance();

        return method.invoke(instance, params);
    }

    protected Object[] createParams(Method method, Request request) throws InstantiationException, IllegalAccessException {
        Parameter[] parameters = method.getParameters();

        Object[] params = new Object[parameters.length];
        int index = 0;

        for(Parameter parameter: parameters){
            if(parameter.isAnnotationPresent(RequestParam.class)){
                RequestParam annotation = parameter.getAnnotation(RequestParam.class);
                params[index++] = request.getParam(annotation.name());
            } else if(parameter.isAnnotationPresent(RequestBody.class)){
                params[index++] = createObjectInstance(parameter.getType(), request);
            }else{
                params[index++] = null;
            }
        }

        return params;
    }

    protected Method findMethod(Class<? extends Annotation> annotation, String path){
        List<Class<?>> classes = ControllerRegistry.getControllers();

        for(Class<?> c: classes){
            Method[] methods = c.getDeclaredMethods();

            for(Method method: methods){
                if(method.isAnnotationPresent(annotation) && isSamePath(annotation, path, method)){
                    return method;
                }
            }
        }

        return null;
    }

    private Object createObjectInstance(Class<?> type, Request request) throws InstantiationException, IllegalAccessException {
        Object o = type.newInstance();

        Field[] fields = o.getClass().getDeclaredFields();

        for(Field field: fields){
            field.setAccessible(true);
            field.set(o, request.getBody(field.getName()));
        }

        return o;
    }

    private boolean isSamePath(Class<? extends Annotation> annotation, String path, Method method) {
        boolean samePath = false;

        if(annotation == GetMapping.class){
            GetMapping getMapping = (GetMapping) method.getAnnotation(annotation);
            samePath = path.equals(getMapping.path());
        } else if(annotation == PostMapping.class){
            PostMapping postMapping = (PostMapping) method.getAnnotation(annotation);
            samePath = path.equals(postMapping.path());
        }
        return samePath;
    }
}
