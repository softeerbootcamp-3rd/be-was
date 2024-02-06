package controller;

import model.Board;
import request.http.HttpRequest;
import response.http.HttpResponse;
import service.BoardService;

import java.io.File;
import java.util.List;

import static util.Uri.*;
import static util.StatusCode.*;

public class HomeController implements Controller {
    private volatile static HomeController instance;
    private final BoardService boardService = BoardService.getInstance();

    private HomeController() {
    }

    public static HomeController getInstance() {
        if (instance == null) {
            instance = new HomeController();
        }

        return instance;
    }

    @Override
    public HttpResponse handleUserRequest(HttpRequest httpRequest) throws Exception {
        String uri = httpRequest.getUri();
        String filePath = httpRequest.getFilePath(uri);
        String method = httpRequest.getMethod();

        File file = new File(filePath);

        if (file.exists() && !file.isDirectory() && method.equals("GET")) {
            StringBuilder builder = new StringBuilder();
            List<Board> boards = boardService.findAll();

            if (boards.isEmpty() || file.getPath().contains("/static")) {
                return new HttpResponse(OK, filePath);
            }

            for (Board board : boards) {
                builder.append("<li>\n");
                builder.append("  <div class=\"wrap\">\n");
                builder.append("      <div class=\"main\">\n");
                builder.append("          <strong class=\"subject\">\n");
                builder.append("              <a href=\"board/show.html/")
                        .append(board.getId()).append("\">").append(board.getTitle()).append("</a>\n");
                builder.append("          </strong>\n");
                builder.append("          <div class=\"auth-info\">\n");
                builder.append("              <i class=\"icon-add-comment\"></i>\n");
                builder.append("              <span class=\"time\">").append(board.getCreatedDate()).append("</span>\n");
                builder.append("              <a href=# class=\"author\">").append(board.getWriter()).append("</a>\n");
                builder.append("          </div>\n");
                builder.append("      <div class=\"reply\"><img src=\"").append(board.getFileUpload())
                        .append("\" alt=\"\" width=45 height=55>\n").append("</div>\n");
                builder.append("      </div>\n");
                builder.append("  </div>\n");
                builder.append("</li>\n");
            }


            return new HttpResponse(OK, filePath, builder);
        }

        if (uri.equals(HOME.getUri())) {
            return new HttpResponse(FOUND, "text/html", HOME_INDEX.getUri(), null);
        }

        return new HttpResponse(NOT_FOUND);
    }
}
