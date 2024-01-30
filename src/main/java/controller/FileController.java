package controller;

import constant.HttpHeader;
import db.PostDatabase;
import dto.HttpRequestDto;
import dto.HttpResponseDto;
import dto.HttpResponseDtoBuilder;
import model.Post;
import util.HttpResponseUtil;
import util.HttpRequestParser;

import java.net.URLEncoder;
import java.util.Map;

public class FileController implements Controller {

    @Override
    public HttpResponseDto handleRequest(HttpRequestDto request) {
        if (request.getMethod().equalsIgnoreCase("GET") && request.getUri().startsWith("/download")) {
            return downloadFile(request);
        }

        return HttpResponseUtil.buildErrorResponse("404", "Not Found", "지원하지 않는 요청입니다.");
    }

    public HttpResponseDto downloadFile(HttpRequestDto request) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();
        Map<String, String> requestParams = HttpRequestParser.parseQueryString(request.getUri());

        if (requestParams.get("postId") != null) {
            Post post = PostDatabase.findPostById(requestParams.get("postId"));

            if (post != null && post.getFile() != null) {
                String fileName = URLEncoder.encode(post.getFileName());
                return responseDtoBuilder.response200Header()
                        .setHeaders(HttpHeader.CONTENT_TYPE, "application/octet-stream")
                        .setHeaders(HttpHeader.CONTENT_LENGTH, Integer.toString(post.getFile().length))
                        .setHeaders(HttpHeader.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .setBody(post.getFile())
                        .build();
            }
        }
        return HttpResponseUtil.buildErrorResponse("404", "Not Found", "파일이 존재하지 않습니다.");
    }
}
