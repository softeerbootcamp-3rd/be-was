package handler;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestMapping;
import config.AppConfig;
import dto.HttpResponseDto;
import exception.BadRequestException;
import exception.InternalServerException;
import model.http.ContentType;
import model.http.HttpMethod;
import model.http.Status;
import model.http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import static config.AppConfig.*;
import static config.AppConfig.userController;

public class DynamicResponseHandlerImpl implements DynamicResponseHandler {
    private static class DynamicResponseHandlerHolder {
        private static final DynamicResponseHandler INSTANCE = new DynamicResponseHandlerImpl(urlControllerMapper());
    }

    private static final Logger logger = LoggerFactory.getLogger(DynamicResponseHandler.class);

    public static DynamicResponseHandler getInstance() {
        return DynamicResponseHandlerHolder.INSTANCE;
    }
    private final UrlControllerMapper urlControllerMapper;

    public DynamicResponseHandlerImpl(UrlControllerMapper urlControllerMapper) {
        this.urlControllerMapper = urlControllerMapper;
    }
    public void handleRequest(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        String url = httpRequest.getStartLine().getPathUrl();
        Method method = urlControllerMapper.getMethod(url);
        try {
            if (method != null) {
                Object controller = urlControllerMapper.getController(method.getDeclaringClass().getName());
                logger.debug(method.getName() + "호출");
                if (method.isAnnotationPresent(GetMapping.class) && httpRequest.getStartLine().getMethod() == HttpMethod.GET) {
                    method.invoke(controller, httpRequest, httpResponseDto);
                } else if (method.isAnnotationPresent(PostMapping.class) && httpRequest.getStartLine().getMethod() == HttpMethod.POST) {
                    method.invoke(controller, httpRequest, httpResponseDto);
                } else {
                    logger.error("처리할 수 없는 요청이 들어옴");
                    throw new BadRequestException("해당하는 요청을 처리할 수 없습니다.");
                }
            }
        } catch (InvocationTargetException e) {
            throw new BadRequestException(e.getCause().getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new InternalServerException("서버에서 에러가 발생하였습니다.", e);
        }

    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        try {
            handleRequest(httpRequest, httpResponseDto);
        } catch (BadRequestException e) {
            handleBadRequestException(e, httpResponseDto);
        } catch (InternalServerException e) {
            handleInternalServerError(e, httpResponseDto);
        }
    }

    private void handleInternalServerError(InternalServerException e, HttpResponseDto httpResponseDto) {
        httpResponseDto.setStatus(Status.INTERNAL_SERVER_ERROR);
        httpResponseDto.setContentType(ContentType.PLAIN);
        httpResponseDto.setContent(e.getMessage().getBytes());
        logger.error("Internal Server Error 발생", e.getMessage());
        e.getStackTrace();
    }

    private void handleBadRequestException(BadRequestException e, HttpResponseDto httpResponseDto) {
        httpResponseDto.setStatus(Status.BAD_REQUEST);
        httpResponseDto.setContentType(ContentType.PLAIN);
        httpResponseDto.setContent(e.getMessage().getBytes());
        logger.error("Bad_Request 발생", e.getMessage());
        e.getStackTrace();
    }
}
