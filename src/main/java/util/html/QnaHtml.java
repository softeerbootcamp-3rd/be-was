package util.html;

import com.google.common.base.Strings;
import db.CommentDatabase;
import db.QnaDatabase;
import db.UserDatabase;
import exception.ResourceNotFoundException;
import model.Qna;
import model.User;
import util.web.SharedData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class QnaHtml {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final int pageSize = 10;
    private static final int paginationSize = 5;

    public static String replaceQnaList(String template) {
        if (template == null)
            return "";
        String pageNumber = SharedData.request.get().getParamMap().get("page");
        if (Strings.isNullOrEmpty(pageNumber))
            pageNumber = "1";
        List<Qna> qnaList = new ArrayList<>(QnaDatabase.getPage(pageSize, Integer.parseInt(pageNumber)));

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
        String pageNumber = SharedData.request.get().getParamMap().get("page");
        if (Strings.isNullOrEmpty(pageNumber))
            pageNumber = "1";

        int currentPage = Integer.parseInt(pageNumber);
        int startPage = ((currentPage - 1) / paginationSize) * paginationSize + 1;
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
            if (currentPage == i)
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
        String qnaIdString = SharedData.request.get().getParamMap().get("qnaId");
        if (qnaIdString == null) {
            throw new IllegalArgumentException();
        }
        Long qnaId = Long.valueOf(qnaIdString);
        Qna qna = QnaDatabase.findById(qnaId);
        if (qna == null)
            throw new ResourceNotFoundException(SharedData.request.get().getPath());
        return template.replace("<!--title-->", qna.getTitle())
                .replace("<!--writer-->", UserDatabase.findByIdOrEmpty(qna.getWriterId()).getName())
                .replace("<!--create-date-->", dateFormat.format(qna.getCreateDatetime()))
                .replace("<!--contents-->", qna.getContents().replace("\n", "</br>"))
                .replace("<!--qna-id-->", Long.toString(qna.getId()));
    }
}
