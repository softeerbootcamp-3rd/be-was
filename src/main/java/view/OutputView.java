package view;

import dto.RequestDto;

import static webserver.RequestHandler.logger;

public class OutputView {

    public static void printRequestDto(RequestDto requestDto) {
        requestDto.getValues()
                .forEach((key, value) -> logger.debug("{} : {}", key, value));
    }
}