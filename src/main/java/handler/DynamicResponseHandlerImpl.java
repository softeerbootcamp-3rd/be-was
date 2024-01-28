package handler;

import controller.UserController;
import dto.HttpResponseDto;
import exception.BadRequestException;
import model.http.ContentType;
import model.http.Status;
import model.http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static config.AppConfig.*;

public class DynamicResponseHandlerImpl implements DynamicResponseHandler {
    private static class DynamicResponseHandlerHolder {
        private static final DynamicResponseHandler INSTANCE = new DynamicResponseHandlerImpl(userController());
    }

    private static final Logger logger = LoggerFactory.getLogger(DynamicResponseHandler.class);

    public static DynamicResponseHandler getInstance() {
        return DynamicResponseHandlerHolder.INSTANCE;
    }

    private final UserController userController;

    public DynamicResponseHandlerImpl(UserController userController) {
        this.userController = userController;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        try {
            userController.doGet(httpRequest, httpResponseDto);
        } catch (BadRequestException e) {
            handleBadRequestException(e, httpResponseDto);
        }
    }

    private void handleBadRequestException(BadRequestException e, HttpResponseDto httpResponseDto) {
        httpResponseDto.setStatus(Status.BAD_REQUEST);
        httpResponseDto.setContentType(ContentType.PLAIN);
        httpResponseDto.setContent(e.getMessage().getBytes());
        logger.error("Bad_Request 발생", e.getMessage());
        e.getStackTrace();
    }
}
