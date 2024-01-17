package service;

import dto.HttpResponseDto;
import model.http.ContentType;
import model.http.Status;
import model.http.request.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticResponseBuilderImpl implements StaticResponseBuilder{

    private static class StaticResponseBuilderHolder{
        private static final StaticResponseBuilder INSTANCE = new StaticResponseBuilderImpl();
    }
    public static StaticResponseBuilder getInstance(){return StaticResponseBuilderHolder.INSTANCE;}
    public static final String TEMPLATES_RESOURCE = "src/main/resources/templates";
    public static final String STATIC_RESOURCES = "src/main/resources/static";
    @Override
    public void build(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        httpResponseDto.setContent(getFile(httpRequest));
        httpResponseDto.setStatus(Status.OK);
        httpResponseDto.setContentType(getContentType(httpRequest));
    }

    private ContentType getContentType(HttpRequest request) {
        if(request.getHeaders().getAccept().contains("css")){
            return ContentType.CSS;
        }
        if(request.getHeaders().getAccept().contains("js")){
            return ContentType.JAVASCRIPT;
        }
        if(request.getHeaders().getAccept().contains("html")){
            return ContentType.HTML;
        }
        return ContentType.MIME;
    }

    private Path getFilePath(HttpRequest request) {
        String filePath = request.getStartLine().getPathUrl();
        if (filePath.contains("html")) {
            return new File(TEMPLATES_RESOURCE + filePath).toPath();
        }
        return new File(STATIC_RESOURCES + filePath).toPath();
    }
    public byte[] getFile(HttpRequest request) {
        try {
            return Files.readAllBytes(getFilePath(request));
        } catch (IOException e) {
            return new byte[0];
        }
    }
}