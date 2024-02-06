package controller;

import annotation.GetMapping;
import annotation.PostMapping;
import model.Board;
import request.http.HttpRequest;
import response.http.HttpResponse;
import service.BoardService;
import util.AuthFilter;
import util.Parser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static util.StatusCode.*;
import static util.Uri.HOME_INDEX;
import static util.Uri.USER_LOGIN;

public class BoardController implements Controller {
    private volatile static BoardController instance = new BoardController();
    private static final Map<String, String> container = new HashMap<>();

    private BoardController() {
    }

    public static BoardController getInstance() {
        if (instance == null) {
            instance = new BoardController();
        }
        Method[] methods = BoardController.class.getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(GetMapping.class)) {
                container.put(m.getAnnotation(GetMapping.class).value(), m.getName());
            }
            if (m.isAnnotationPresent(PostMapping.class)) {
                container.put(m.getAnnotation(PostMapping.class).value(), m.getName());
            }
        }

        return instance;
    }

    private final BoardService boardService = BoardService.getInstance();

    @Override
    public HttpResponse handleUserRequest(HttpRequest httpRequest) throws Exception {
        String uri = httpRequest.getUri();
        Integer id = null;

        if (uri.contains("/board/show.html")) {
            uri = "/board/show.html";
            id = Integer.parseInt(httpRequest.getUri().split("/")[3]);
        }
        System.out.println(id);

        String filePath = httpRequest.getFilePath(uri);
        String method = httpRequest.getMethod();
        boolean isLogin = AuthFilter.isLogin(httpRequest);

        File file = new File(filePath);

        if (file.exists() && method.equals("GET") && container.containsKey(uri)) {
            if (id == null) {
                Method declaredMethod = this.getClass().getDeclaredMethod(container.get(uri), String.class);
                boolean requiredLogin = declaredMethod.getAnnotation(GetMapping.class).requiredLogin();

                if (requiredLogin && !isLogin) {
                    return new HttpResponse(FOUND, "text/html", USER_LOGIN.getUri(), null);
                }
                return (HttpResponse) declaredMethod.invoke(this, filePath);
            }
            if (id != null) {
                Method declaredMethod = this.getClass().getDeclaredMethod(container.get(uri), String.class, Integer.class);
                boolean requiredLogin = declaredMethod.getAnnotation(GetMapping.class).requiredLogin();

                if (requiredLogin && !isLogin) {
                    return new HttpResponse(FOUND, "text/html", USER_LOGIN.getUri(), null);
                }
                return (HttpResponse) declaredMethod.invoke(this, filePath, id);
            }
        }

        if (method.equals("POST") && container.containsKey(uri)) {
            Method declaredMethod = this.getClass().getDeclaredMethod(container.get(uri), HttpRequest.class);
            return (HttpResponse) declaredMethod.invoke(this, httpRequest);
        }
        return new HttpResponse(NOT_FOUND);
    }

    @GetMapping(value = "/board/write.html", requiredLogin = true)
    public HttpResponse getBoardWrite(String filePath) {
        try {
            return new HttpResponse(OK, filePath);
        } catch (IOException e) {
            return new HttpResponse(NOT_FOUND);
        }
    }

    @GetMapping(value = "/board/show.html", requiredLogin = true)
    public HttpResponse getBoardShow(String filePath, Integer id) {
        try {
            StringBuilder builder = new StringBuilder();
            Board board = boardService.findBoardById(id);

            builder.append("<header class=\"qna-header\">\n");
            builder.append("  <h2 class=\"qna-title\">").append(board.getTitle()).append("</h2>\n");
            builder.append("</header>\n");
            builder.append("<div class=\"content-main\">\n");
            builder.append("  <article class=\"article\">\n");
            builder.append("      <div class=\"article-header\">\n");
            builder.append("          <div class=\"article-header-thumb\">\n");
            builder.append("              <img src=\"https://graph.facebook.com/v2.3/100000059371774/picture\" class=\"article-author-thumb\" alt=\"\">\n");
            builder.append("          </div>\n");
            builder.append("          <div class=\"article-header-text\">\n");
            builder.append("              <a href=\"/users/").append(board.getWriter())
                    .append("\" class=\"article-author-name\">").append(board.getWriter()).append("</a>\n");
            builder.append("              <a href=\"/questions/")
                    .append("\" class=\"article-header-time\" title=\"퍼머링크\">\n")
                    .append("                  ").append(board.getCreatedAt()).append("\n")
                    .append("                  <i class=\"icon-link\"></i>\n")
                    .append("              </a>\n");
            builder.append("          </div>\n");
            builder.append("      </div>\n");
            builder.append("      <div class=\"article-doc\">\n");
            builder.append("          <p>").append(board.getContent()).append("</p>\n");
            builder.append("      </div>\n");
            builder.append("      <img src=\"").append(board.getFileUpload()).append("\" alt=\"\" width=200 height=240>\n");
            builder.append("  </article>\n");
            builder.append("</div>\n");

            return new HttpResponse(OK, filePath, builder);
        } catch (IOException e) {
            return new HttpResponse(NOT_FOUND);
        }
    }

    @PostMapping(value = "/board/create", requiredLogin = true)
    public HttpResponse boardWrite(HttpRequest httpRequest) {
        try {
            Board board = Parser.jsonParser(Board.class, httpRequest.getRequestBodyB());
            boardService.write(board);

            return new HttpResponse(FOUND, "text/html", HOME_INDEX.getUri(), null);
        } catch (Exception e) {
            return new HttpResponse(NOT_FOUND);
        }
    }
}
