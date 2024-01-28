package handler;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestMapping;
import dto.HttpResponseDto;
import exception.BadRequestException;
import model.http.ContentType;
import model.http.HttpMethod;
import model.http.Status;
import model.http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import static config.AppConfig.userController;

public class DynamicResponseHandlerImpl implements DynamicResponseHandler {
    private static class DynamicResponseHandlerHolder {
        private static final DynamicResponseHandler INSTANCE = new DynamicResponseHandlerImpl();
    }

    private static final Logger logger = LoggerFactory.getLogger(DynamicResponseHandler.class);

    public static DynamicResponseHandler getInstance() {
        return DynamicResponseHandlerHolder.INSTANCE;
    }

    private final HashMap<String, Method> urlMapper = new HashMap<>();
    private final HashMap<String, Object> controllerMapper = new HashMap<>();

    public DynamicResponseHandlerImpl() {
        register(userController());
    }
    public void register(Object controller) {
        logger.debug("Controller Mapper에 컨트롤러 등록");
        Class<?> clazz = controller.getClass();
        controllerMapper.put(clazz.getName(), controller);
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping classAnnotation = clazz.getAnnotation(RequestMapping.class);
            String baseUrl = classAnnotation.path();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    GetMapping getAnnotation = method.getAnnotation(GetMapping.class);
                    String url = baseUrl + getAnnotation.value();
                    urlMapper.put(url, method);
                } else if (method.isAnnotationPresent(PostMapping.class)) {
                    PostMapping postAnnotation = method.getAnnotation(PostMapping.class);
                    String url = baseUrl + postAnnotation.value();
                    urlMapper.put(url, method);
                }
            }
        }
    }

    public void handleRequest(HttpRequest httpRequest, HttpResponseDto httpResponseDto) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        String url = httpRequest.getStartLine().getPathUrl();
        Method method = urlMapper.get(url);
        if (method != null) {
            Object controller = controllerMapper.get(method.getDeclaringClass().getName());
            logger.debug(method.getName() + "호출");
            if(method.isAnnotationPresent(GetMapping.class) && httpRequest.getStartLine().getMethod() == HttpMethod.GET){
                method.invoke(controller, httpRequest, httpResponseDto);
            } else if (method.isAnnotationPresent(PostMapping.class) && httpRequest.getStartLine().getMethod() == HttpMethod.POST) {
                method.invoke(controller, httpRequest, httpResponseDto);
            }
            else{
                logger.error("처리할 수 없는 요청이 들어옴");
                throw new BadRequestException("해당하는 요청을 처리할 수 없습니다.");
            }
        }
    }
    @Override
    public void handle(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        try {
            handleRequest(httpRequest, httpResponseDto);
        } catch (InvocationTargetException e) {
            handleBadRequestException(e, httpResponseDto);
        } catch (IllegalAccessException | InstantiationException e) {
            e.getStackTrace();
        }
    }

    private void handleBadRequestException(InvocationTargetException e, HttpResponseDto httpResponseDto) {
        httpResponseDto.setStatus(Status.BAD_REQUEST);
        httpResponseDto.setContentType(ContentType.PLAIN);
        httpResponseDto.setContent(e.getCause().getMessage().getBytes());
        logger.error("Bad_Request 발생", e.getMessage());
        e.getStackTrace();
    }
}
