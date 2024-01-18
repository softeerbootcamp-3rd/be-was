package view;

import dto.RequestHeaderDto;
import dto.RequestLineDto;

import java.lang.reflect.Field;

import static webserver.RequestHandler.logger;

public class OutputView {

    public static void printRequest(RequestLineDto requestLineDto, RequestHeaderDto requestHeaderDto) throws IllegalAccessException {
        printRequestLine(requestLineDto);
        printRequestHeader(requestHeaderDto);
    }

    private static void printRequestLine(RequestLineDto requestLineDto) throws IllegalAccessException {
        for (Field field : requestLineDto.getClass().getDeclaredFields()){
            field.setAccessible(true);
            logger.debug("{} : {}", field.getName(), field.get(requestLineDto));
        }
    }

    public static void printRequestHeader(RequestHeaderDto requestHeaderDto) throws IllegalAccessException {
        for (Field field : requestHeaderDto.getClass().getDeclaredFields()){
            field.setAccessible(true);
            logger.debug("{} : {}", field.getName(), field.get(requestHeaderDto));
        }
    }
}