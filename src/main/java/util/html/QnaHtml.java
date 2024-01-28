package util.html;

import com.google.common.base.Strings;
import constant.HtmlTemplate;
import db.CommentDatabase;
import db.QnaDatabase;
import db.UserDatabase;
import exception.ResourceNotFoundException;
import model.Comment;
import model.Qna;
import model.User;
import util.web.SharedData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QnaHtml {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final int pageSize = 10;
    private static final int paginationSize = 5;

    public static String replaceQnaList(String template) {
        if (template == null)
            return "";
        int pageNumber = SharedData.getParamDataOrElse("page", Integer.class, 1);
        List<Qna> qnaList = new ArrayList<>(QnaDatabase.getPage(pageSize, pageNumber));

        StringBuilder sb = new StringBuilder();
        for (Qna qna : qnaList) {
            User writer = UserDatabase.findByIdOrEmpty(qna.getWriterId());
            sb.append(template.replace("<!--qna-id-->", Long.toString(qna.getId()))
                    .replace("<!--title-->", qna.getTitle())
                    .replace("<!--create-date-->", dateFormat.format(qna.getCreateDatetime()))
                    .replace("<!--user-name-->", writer.getName())
                    .replace("<!--comments-->", Long.toString(CommentDatabase.countByQnaId(qna.getId()))));
        }
        return sb.toString();
    }

    public static String replacePagination(String template) {
        if (template == null)
            return "";

        int pageNumber = SharedData.getParamDataOrElse("page", Integer.class, 1);
        int startPage = ((pageNumber - 1) / paginationSize) * paginationSize + 1;
        int endPage = startPage + paginationSize - 1;

        int totalPages = (int) Math.ceil((double) QnaDatabase.countAll() / pageSize);
        if (totalPages == 0) totalPages = 1;

        StringBuilder sb = new StringBuilder();
        if (startPage > 1)
            sb.append(template.replace("<!--link-->", Integer.toString(startPage - 1))
                    .replace("<!--page-number-->", "«"));
        for (int i = startPage; i <= Math.min(totalPages, endPage); i++) {
            String temp = template.replace("<!--link-->", Integer.toString(i))
                    .replace("<!--page-number-->", Integer.toString(i));
            if (pageNumber == i)
                temp = temp.replace("<!--active-->", "active");
            sb.append(temp);
        }
        if (endPage < totalPages)
            sb.append(template.replace("<!--link-->", Integer.toString(endPage + 1))
                    .replace("<!--page-number-->", "»"));
        return sb.toString();
    }

    public static String replaceQna(String template) {
        if (template == null)
            return "";
        Long qnaId = SharedData.getParamDataNotEmpty("qnaId", Long.class);
        Qna qna = QnaDatabase.findById(qnaId);
        if (qna == null)
            throw new ResourceNotFoundException(SharedData.request.get().getPath());
        return template.replace("<!--title-->", qna.getTitle())
                .replace("<!--writer-->", UserDatabase.findByIdOrEmpty(qna.getWriterId()).getName())
                .replace("<!--create-date-->", dateFormat.format(qna.getCreateDatetime()))
                .replace("<!--contents-->", qna.getContents().replace("\n", "</br>"))
                .replace("<!--qna-id-->", Long.toString(qna.getId()))
                .replace("<!--comment-count-->", Long.toString(CommentDatabase.countByQnaId(qnaId)))
                .replace("<!--comments-->", replaceComments(HtmlTemplate.QNA_COMMENTS.getTemplate()));
    }

    public static String replaceComments(String template) {
        if (template == null)
            return "";
        Long qnaId = SharedData.getParamDataNotEmpty("qnaId", Long.class);
        Collection<Comment> comments = CommentDatabase.findByQnaId(qnaId);

        StringBuilder sb = new StringBuilder();
        for (Comment comment : comments) {
            User writer = UserDatabase.findByIdOrEmpty(comment.getWriterId());
            sb.append(template.replace("<!--writer-->", writer.getName())
                    .replace("<!--create-date-->", dateFormat.format(comment.getCreateDatetime()))
                    .replace("<!--content-->", comment.getContent().replace("\n", "</br>")));
        }
        return sb.toString();
    }
}
