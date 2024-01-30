package util.html;

import database.AttachmentRepository;
import database.CommentRepository;
import database.BoardRepository;
import database.UserRepository;
import exception.ResourceNotFoundException;
import model.Attachment;
import model.Board;
import model.Comment;
import model.User;
import util.web.SharedData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class BoardHtml {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final int pageSize = 10;
    private static final int paginationSize = 5;

    public static String boardList(String template) {
        int pageNumber = SharedData.getParamDataOrElse("page", Integer.class, 1);
        List<Board> boardList = new ArrayList<>(BoardRepository.getPage(pageSize, pageNumber));

        StringBuilder sb = new StringBuilder();
        for (Board board : boardList) {
            User writer = UserRepository.findByIdOrEmpty(board.getWriterId());
            sb.append(template.replace("<!--board-id-->", "" + board.getId())
                    .replace("<!--title-->", board.getTitle())
                    .replace("<!--create-date-->", dateFormat.format(board.getCreateDatetime()))
                    .replace("<!--user-name-->", writer.getName())
                    .replace("<!--comments-->", "" + CommentRepository.countByBoardId(board.getId())));
        }
        return sb.toString();
    }

    public static String pagination(String template) {
        int pageNumber = SharedData.getParamDataOrElse("page", Integer.class, 1);
        int startPage = ((pageNumber - 1) / paginationSize) * paginationSize + 1;
        int endPage = startPage + paginationSize - 1;

        int totalPages = (int) Math.ceil((double) BoardRepository.countAll() / pageSize);
        if (totalPages == 0) totalPages = 1;

        StringBuilder sb = new StringBuilder();
        if (startPage > 1)
            sb.append(template.replace("<!--link-->", "" + (startPage - 1))
                    .replace("<!--page-number-->", "«"));
        for (int i = startPage; i <= Math.min(totalPages, endPage); i++) {
            String temp = template.replace("<!--link-->", "" + i)
                    .replace("<!--page-number-->", "" + i);
            if (pageNumber == i)
                temp = temp.replace("<!--active-->", "active");
            sb.append(temp);
        }
        if (endPage < totalPages)
            sb.append(template.replace("<!--link-->", "" + (endPage + 1))
                    .replace("<!--page-number-->", "»"));
        return sb.toString();
    }

    public static String boardContent(String template) {
        Long boardId = SharedData.getParamDataNotEmpty("boardId", Long.class);
        Board board = BoardRepository.findById(boardId);
        if (board == null)
            throw new ResourceNotFoundException(SharedData.request.get().getPath());
        return template.replace("<!--title-->", board.getTitle())
                .replace("<!--writer-->", UserRepository.findByIdOrEmpty(board.getWriterId()).getName())
                .replace("<!--create-date-->", dateFormat.format(board.getCreateDatetime()))
                .replace("<!--contents-->", "<p>" + board.getContents().replace("\n", "</br>") + "</p>")
                .replace("<!--board-id-->", "" + board.getId())
                .replace("<!--comment-count-->", "" + CommentRepository.countByBoardId(boardId));
    }

    public static String attachment(String template) {
        Long boardId = SharedData.getParamDataNotEmpty("boardId", Long.class);
        Attachment attachment = AttachmentRepository.findByBoardId(boardId);
        if (attachment == null)
            return "";
        return template.replace("<!--link-->", "/attachment/download?boardId=" + boardId)
                .replace("<!--filename-->", attachment.getFilename());
    }

    public static String preview(String template) {
        Long boardId = SharedData.getParamDataNotEmpty("boardId", Long.class);
        Attachment attachment = AttachmentRepository.findByBoardId(boardId);
        if (attachment == null)
            return "";
        return template.replace("<!--board-id-->", "" + boardId);
    }

    public static String boardBtnGroup(String template) {
        Long boardId = SharedData.getParamDataNotEmpty("boardId", Long.class);
        Board board = BoardRepository.findById(boardId);
        User user = SharedData.requestUser.get();
        assert board != null;
        if (!Objects.equals(user.getUserId(), board.getWriterId()))
            return "";
        return template.replace("<!--board-id-->", "" + boardId);
    }

    public static String comments(String template) {
        Long boardId = SharedData.getParamDataNotEmpty("boardId", Long.class);
        Collection<Comment> comments = CommentRepository.findByBoardId(boardId);

        StringBuilder sb = new StringBuilder();
        for (Comment comment : comments) {
            User writer = UserRepository.findByIdOrEmpty(comment.getWriterId());
            sb.append(template.replace("<!--writer-->", writer.getName())
                    .replace("<!--create-date-->", dateFormat.format(comment.getCreateDatetime()))
                    .replace("<!--content-->", comment.getContents().replace("\n", "</br>"))
                    .replace("<!--comment-btn-group-->", commentBtnGroup(boardId, comment)));
        }
        return sb.toString();
    }

    private static String commentBtnGroup(Long boardId, Comment comment) {
        User user = SharedData.requestUser.get();
        if (user == null || !Objects.equals(user.getUserId(), comment.getWriterId()))
            return "";
        return HtmlTemplates.get("<!--comment-btn-group-->").getTemplate()
                .replace("<!--board-id-->", "" + boardId)
                .replace("<!--comment-id-->", "" + comment.getId());
    }

    public static String commentForm(String template) {
        Long boardId = SharedData.getParamDataNotEmpty("boardId", Long.class);
        return template.replace("<!--board-id-->", "" + boardId);
    }
}
