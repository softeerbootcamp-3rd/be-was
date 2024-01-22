package common.view;

import dto.RequestLineDto;

import java.lang.reflect.Field;

import static webserver.RequestHandler.logger;

public class OutputView {

    public static void printRequest(RequestLineDto requestLineDto) throws IllegalAccessException {
        printRequestLine(requestLineDto);
    }

    private static void printRequestLine(RequestLineDto requestLineDto) throws IllegalAccessException {
        for (Field field : requestLineDto.getClass().getDeclaredFields()){
            field.setAccessible(true);
            logger.debug("{} : {}", field.getName(), field.get(requestLineDto));
        }
    }
}
