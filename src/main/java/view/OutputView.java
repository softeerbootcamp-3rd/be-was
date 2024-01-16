package view;

import dto.RequestDto;

import java.lang.reflect.Field;

import static webserver.RequestHandler.logger;

public class OutputView {

    public static void printRequestDto(RequestDto requestDto) throws IllegalAccessException {
        for (Field field : requestDto.getClass().getDeclaredFields()){
            field.setAccessible(true);
            logger.debug("{} : {}", field.getName(), field.get(requestDto));
        }
    }
}