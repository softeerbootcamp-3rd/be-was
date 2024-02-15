package repository;

import com.google.common.collect.Maps;

import model.Qna;

import java.util.Collection;
import java.util.Map;

public class QnaRepository {
    private static final Map<String, Qna> qnas = Maps.newHashMap();

    public void addQna(Qna qna) {
        qnas.put(qna.getWriter(), qna);
    }

    public Qna findQnaByUsername(String username) {
        return qnas.get(username);
    }

    public Collection<Qna> findAllQnas() {
        return qnas.values();
    }

}
