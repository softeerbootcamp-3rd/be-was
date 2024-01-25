package handler;

import dto.HttpResponseDto;
import exception.NotFound;
import model.http.ContentType;
import model.http.Status;
import model.http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileDetector;

import static config.AppConfig.fileDetector;

public class StaticResponseHandlerImpl implements StaticResponseHandler {
    private final FileDetector fileDetector;

    public StaticResponseHandlerImpl(FileDetector fileDetector) {
        this.fileDetector = fileDetector;
    }

    private static class StaticResponseHandlerHolder {
        private static final StaticResponseHandler INSTANCE = new StaticResponseHandlerImpl(fileDetector());
    }

    public static StaticResponseHandler getInstance() {
        return StaticResponseHandlerHolder.INSTANCE;
    }

    private static final Logger logger = LoggerFactory.getLogger(StaticResponseHandler.class);

    @Override
    public void handle(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        try {
            handleStaticFileRequest(httpRequest, httpResponseDto);
        } catch (NotFound e) {
            handleNotFound(httpResponseDto, e);
        }
    }

    private static void handleNotFound(HttpResponseDto httpResponseDto, NotFound e) {
        logger.error("파일을 찾을 수 없습니다." + e.getMessage());
        httpResponseDto.setStatus(Status.REDIRECT);
        httpResponseDto.addHeader("Location","/error/not_found.html");
        httpResponseDto.setContentType(ContentType.HTML);
    }

    private void handleStaticFileRequest(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        httpResponseDto.setContent(fileDetector.getFileByte(httpRequest.getStartLine().getPathUrl()));
        httpResponseDto.setStatus(Status.OK);
        httpResponseDto.setContentType(fileDetector.getContentType(httpRequest.getHeaders().getAccept(), httpRequest.getStartLine().getPathUrl()));
    }
}
