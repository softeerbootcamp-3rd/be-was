package controller;

import constant.HttpHeader;
import dto.HttpRequestDto;
import dto.HttpResponseDto;
import dto.HttpResponseDtoBuilder;
import dto.PostDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.QnaService;
import util.HtmlBuilder;
import util.HttpResponseUtil;
import util.MultipartFormDataParser;
import util.WebUtil;

import java.util.Map;

public class QnaController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final QnaService qnaService;

    public QnaController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @Override
    public HttpResponseDto handleRequest(HttpRequestDto request) {
        if (request.getMethod().equalsIgnoreCase("GET") && request.getUri().startsWith("/qna/form")) {
            return printWriteForm(request);
        }
        if (request.getMethod().equalsIgnoreCase("GET") && request.getUri().startsWith("/qna/show")) {
            return printWriteDetailPage(request);
        }
        if (request.getMethod().equalsIgnoreCase("POST") && request.getUri().startsWith("/qna/write")) {
            return writePost(request);
        }

        return HttpResponseUtil.loadResource(request, logger);
    }

    public HttpResponseDto printWriteForm(HttpRequestDto request) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();
        if (request.getUser() != null) {
            System.out.println("print write form >>>> ");
            // 로그인 한 경우
            byte[] body = HtmlBuilder.buildWriteFormPage(request);

            return responseDtoBuilder.response200Header()
                    .setHeaders(HttpHeader.CONTENT_TYPE, WebUtil.getContentType(request.getUri()) + ";charset=utf-8")
                    .setHeaders(HttpHeader.CONTENT_LENGTH, Integer.toString(body.length))
                    .setBody(body)
                    .build();
        }

        return responseDtoBuilder.response302Header()
                .setHeaders(HttpHeader.LOCATION, "/user/login.html").build();
    }

    public HttpResponseDto writePost(HttpRequestDto request) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();
        PostDto postDto = MultipartFormDataParser.parseMultipartFormData(request);
        if (request.getUser() != null) {
            try {
                String postId = qnaService.writePost(postDto, request.getUser());

                return responseDtoBuilder.response302Header()
                        .setHeaders(HttpHeader.LOCATION, "/qna/show?postId=" + postId).build();
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage());

                return HttpResponseUtil.buildErrorResponse("400", "Bad Request", e.getMessage());
            }
        }

        return responseDtoBuilder.response302Header()
                .setHeaders(HttpHeader.LOCATION, "/user/login.html").build();
    }

    public HttpResponseDto printWriteDetailPage(HttpRequestDto request) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();
        if (request.getUser() == null) {
            // 로그인하지 않은 경우
            return responseDtoBuilder.response302Header()
                    .setHeaders(HttpHeader.LOCATION, "/user/login.html").build();
        }
        Map<String, String> queryString = WebUtil.parseQueryString(request.getUri());
        try {
            byte[] body = qnaService.printWriteDetailPage(queryString, request.getUser());

            return responseDtoBuilder.response200Header()
                    .setHeaders(HttpHeader.CONTENT_TYPE, "text/html;charset=utf-8")
                    .setHeaders(HttpHeader.CONTENT_LENGTH, Integer.toString(body.length))
                    .setBody(body)
                    .build();
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());

            return HttpResponseUtil.buildErrorResponse("400", "Bad Request", e.getMessage());
        }
    }
}
