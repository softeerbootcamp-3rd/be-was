package db;

import com.google.common.collect.Maps;
import model.Qna;
import model.User;

import java.util.Collection;
import java.util.Map;

public class QnaRepository {
    private static final QnaRepository instance = new QnaRepository();
    private static Long seq = 0L;
    private static Map<Long, Qna> qnas = Maps.newHashMap();

    public static QnaRepository getInstance() {
        return instance;
    }

    public void addQna(Qna qna) {
        qna.setId(++seq);
        qnas.put(seq, qna);
    }

    public Qna findQnaById(Long Id) {
        return qnas.get(Id);
    }

    public Collection<Qna> findAll() {
        return qnas.values();
    }
}
