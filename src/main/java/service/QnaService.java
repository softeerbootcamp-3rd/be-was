package service;

import db.Database;
import model.Qna;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class QnaService {
    private final static Logger logger = LoggerFactory.getLogger(QnaService.class);

    public void addQna(String writer, String title, String contents) {
        Qna qna = new Qna(writer, title, contents,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Database.addQna(qna);

        logger.debug("qna가 추가되었습니다. qna-writer = {}", qna.getWriter());
    }
}
