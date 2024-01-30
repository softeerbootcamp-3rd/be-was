package controller;

import annotation.Controller;
import annotation.RequestBody;
import annotation.RequestMapping;
import com.google.common.base.Strings;
import constant.HttpStatus;
import database.CommentRepository;
import database.AttachmentRepository;
import database.BoardRepository;
import dto.CommentDto;
import dto.BoardDto;
import entity.Board;
import entity.Comment;
import entity.Attachment;
import entity.User;
import model.MultipartFile;
import model.SharedData;
import util.FileUtils;
import webserver.HttpResponse;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Controller
public class BoardController {

    @RequestMapping(method = "POST", path = "/board/post")
    public static HttpResponse boardBoard(@RequestBody BoardDto board) throws IOException {
        User currentUser = SharedData.requestUser.get();
        if (currentUser == null)
            return HttpResponse.of(HttpStatus.FORBIDDEN);

        Long boardId = BoardRepository.add(new Board(currentUser.getUserId(), board.getTitle(), board.getContents(), new Date()));
        MultipartFile file = board.getFile();
        if (board.getFile() != null) {
            String savedPath = FileUtils.saveMultipartFile(file);
            AttachmentRepository.add(new Attachment(boardId, file.getFilename(), file.getContentType(), savedPath));
        }
        return HttpResponse.redirect("/index.html");
    }

    @RequestMapping(method = "POST", path = "/board/{boardId}/comment")
    public static HttpResponse boardComment(@RequestBody CommentDto comment) {
        User currentUser = SharedData.requestUser.get();
        if (currentUser == null)
            return HttpResponse.of(HttpStatus.FORBIDDEN);

        String boardIdString = SharedData.pathParams.get().get("boardId");
        if (Strings.isNullOrEmpty(boardIdString))
            return HttpResponse.of(HttpStatus.BAD_REQUEST);

        Long boardId = Long.valueOf(boardIdString);
        CommentRepository.add(new Comment(boardId, currentUser.getUserId(), comment.getContent(), new Date()));
        return HttpResponse.redirect("/board/show.html?boardId=" + boardIdString);
    }

    @RequestMapping(method = "POST", path = "/board/{boardId}/delete")
    public static HttpResponse deleteBoard() {
        String boardIdString = SharedData.pathParams.get().get("boardId");
        if (Strings.isNullOrEmpty(boardIdString))
            return HttpResponse.of(HttpStatus.BAD_REQUEST);
        Long boardId = Long.valueOf(boardIdString);
        Board board = BoardRepository.findById(boardId);

        User currentUser = SharedData.requestUser.get();
        if (currentUser == null || !Objects.equals(currentUser.getUserId(), board.getWriterId()))
            return HttpResponse.of(HttpStatus.FORBIDDEN);

        BoardRepository.deleteById(boardId);
        AttachmentRepository.deleteByBoardId(boardId);
        CommentRepository.deleteByBoardId(boardId);
        return HttpResponse.redirect("/index.html");
    }

}
