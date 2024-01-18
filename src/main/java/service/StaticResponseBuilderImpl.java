package service;

import config.AppConfig;
import dto.HttpResponseDto;
import model.http.ContentType;
import model.http.Status;
import model.http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileDetector;
import exception.NotFound;

public class StaticResponseBuilderImpl implements StaticResponseBuilder{
    private final FileDetector fileDetector;

    public StaticResponseBuilderImpl(FileDetector fileDetector) {
        this.fileDetector = fileDetector;
    }

    private static class StaticResponseBuilderHolder{
        private static final StaticResponseBuilder INSTANCE = new StaticResponseBuilderImpl(AppConfig.fileDetector());
    }
    public static StaticResponseBuilder getInstance(){return StaticResponseBuilderHolder.INSTANCE;}
    private static final Logger logger = LoggerFactory.getLogger(StaticResponseBuilder.class);
    @Override
    public void build(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        try {
            httpResponseDto.setContent(fileDetector.getFile(httpRequest.getStartLine().getPathUrl()));
            httpResponseDto.setStatus(Status.OK);
            httpResponseDto.setContentType(fileDetector.getContentType(httpRequest.getHeaders().getAccept()));
        } catch (NotFound e) {
            logger.error("파일을 찾을 수 없습니다." + e.getMessage());
            httpResponseDto.setStatus(Status.REDIRECT);
            httpResponseDto.setContentType(ContentType.HTML);
            httpResponseDto.setLocation("/error/not_found.html");
            httpResponseDto.setContent(fileDetector.getNotFound());
        }
    }
}