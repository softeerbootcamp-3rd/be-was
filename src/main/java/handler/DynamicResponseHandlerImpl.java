package handler;

import config.AppConfig;
import controller.UserController;
import dto.HttpResponseDto;
import exception.BadRequestException;
import exception.InternalServerError;
import model.http.ContentType;
import model.http.Status;
import model.http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicResponseHandlerImpl implements DynamicResponseHandler {
    private static class DynamicResponseHandlerHolder {
        private static final DynamicResponseHandler INSTANCE = new DynamicResponseHandlerImpl();
    }
    private static final Logger logger = LoggerFactory.getLogger(DynamicResponseHandler.class);
    public static DynamicResponseHandler getInstance(){
        return DynamicResponseHandlerHolder.INSTANCE;
    }
    @Override
    public void handle(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        try{
        UserController userController = AppConfig.userController();
        userController.doGet(httpRequest, httpResponseDto);
        } catch (BadRequestException e) {
            httpResponseDto.setStatus(Status.BAD_REQUEST);
            httpResponseDto.setContentType(ContentType.PLAIN);
            httpResponseDto.setContent(e.getMessage().getBytes());
            logger.error("Bad_Request 발생" + e.getMessage());
        }catch (InternalServerError e){
            httpResponseDto.setStatus(Status.INTERNAL_SERVER_ERROR);
            logger.error("InternalServerError 발생, " + e.getMessage());
        }
    }
}