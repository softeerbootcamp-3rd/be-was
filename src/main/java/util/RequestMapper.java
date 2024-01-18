package util;

import annotation.RequestMapping;
import annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatus;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.util.stream.IntStream;

public class RequestMapper {
    private static final Logger logger = LoggerFactory.getLogger(RequestMapper.class);
    public static final Map<String, Method> URL_MAPPING = new HashMap<>();

    static {
        List<Class<?>> controllers = scanControllers("controller");

        Class<? extends Annotation> requestMapping = RequestMapping.class;
        for (Class<?> c : controllers) {
            Method[] methods = c.getDeclaredMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(requestMapping)) {
                    RequestMapping requestInfo = (RequestMapping) method.getAnnotation(requestMapping);
                    URL_MAPPING.put(requestInfo.method() + " " + requestInfo.path(), method);
                }
            }
        }
    }

    public static List<Class<?>> scanControllers(String basePackage) {
        List<Class<?>> controllerClasses = new ArrayList<>();

        String basePath = basePackage.replace(".", File.separator);
        String packagePath = "src/main/java/" + basePath;

        List<File> classFiles = listFiles(packagePath);

        for (File file : classFiles) {
            String className = getClassName(file, basePath);
            try {
                Class<?> clazz = Class.forName(className);
                if (hasControllerAnnotation(clazz)) controllerClasses.add(clazz);
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
            }
        }

        return controllerClasses;
    }

    private static List<File> listFiles(String directoryPath) {
        List<File> classFiles = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && (file.getName().endsWith(".java") || file.getName().endsWith(".class")))
                        classFiles.add(file);
                }
            }
        }

        return classFiles;
    }

    private static String getClassName(File file, String basePath) {
        String fileName = file.getAbsolutePath().replace(File.separator, ".");
        int startIndex = fileName.indexOf(basePath);
        int endIndex = fileName.lastIndexOf(".");
        return fileName.substring(startIndex, endIndex);
    }

    private static boolean hasControllerAnnotation(Class<?> clazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getSimpleName().equals("Controller")) return true;
        }
        return false;
    }

    public static Method getMethod(HttpRequest request) {
        return URL_MAPPING.get(request.getMethod() + " " + request.getPath());
    }

    public static HttpResponse invoke(Method method, HttpRequest request) {
        try {
            Class<?> clazz = method.getDeclaringClass();
            Object instance = clazz.getDeclaredConstructor().newInstance();

            Object result = method.invoke(instance, mapParams(method, request));
            if (result instanceof HttpResponse) return (HttpResponse) result;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        return HttpResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(HttpStatus.INTERNAL_SERVER_ERROR.getFullMessage())
                .build();
    }

    private static Object[] mapParams(Method method, HttpRequest request) {
        Class<? extends Annotation> requestParam = RequestParam.class;
        Parameter[] parameters = method.getParameters();
        Object[] params = new Object[parameters.length];

        IntStream.range(0, parameters.length)
                .forEach(i -> {
                    Parameter parameter = parameters[i];
                    if(parameter.isAnnotationPresent(requestParam)){
                        RequestParam annotation = (RequestParam) parameter.getAnnotation(requestParam);

                        params[i]= request.getParamMap().get(annotation.value());
                    }
                });

        return params;
    }
}
