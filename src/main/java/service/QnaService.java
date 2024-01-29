package service;

import db.Database;
import db.QnaRepository;
import model.Qna;

import java.util.Collection;
import java.util.List;

public class QnaService {
    private final Database database = Database.getInstance();
    private static final QnaService instance = new QnaService();

    public static QnaService getInstance(){
        return instance;
    }

    public void create(Qna qna) {
        QnaRepository.addQna(qna);
    }

    public Collection<Qna> getPostList(){
        return QnaRepository.findAll();

    }

}
