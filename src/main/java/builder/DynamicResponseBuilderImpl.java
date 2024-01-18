package builder;

import config.AppConfig;
import dto.HttpResponseDto;
import model.http.ContentType;
import model.http.Status;
import model.http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import exception.BadRequestException;
import exception.InternalServerError;
import webApplicationServer.controller.UserController;

public class DynamicResponseBuilderImpl implements DynamicResponseBuilder {
    private static class DynamicResponseBuilderHolder{
        private static final DynamicResponseBuilder INSTANCE = new DynamicResponseBuilderImpl();
    }
    private static final Logger logger = LoggerFactory.getLogger(DynamicResponseBuilder.class);
    public static DynamicResponseBuilder getInstance(){
        return DynamicResponseBuilderHolder.INSTANCE;
    }
    @Override
    public void build(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
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