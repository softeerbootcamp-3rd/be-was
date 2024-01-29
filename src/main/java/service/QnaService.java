package service;

import db.Database;
import db.QnaRepository;
import model.Qna;
import model.User;

import java.util.Optional;

public class QnaService {
    private final Database database = Database.getInstance();
    private static final QnaService instance = new QnaService();

    public static QnaService getInstance(){
        return instance;
    }

    public void create(Qna qna) {
        QnaRepository.addQna(qna);
    }

//    public Optional<Qna> login(String userId, String password) {
//        return Optional.ofNullable(database.findUserById(userId))
//                .filter(user -> user.getPassword().equals(password));
//    }
}
