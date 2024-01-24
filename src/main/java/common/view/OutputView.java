package common.view;

import dto.HttpRequest;

import java.lang.reflect.Field;

import static webserver.RequestHandler.logger;

public class OutputView {

    public static void printRequest(HttpRequest request) throws IllegalAccessException {
        for (Field field : request.getClass().getDeclaredFields()){
            field.setAccessible(true);
            logger.debug("{} : {}", field.getName(), field.get(request));
        }
    }
}
