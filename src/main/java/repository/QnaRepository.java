package repository;

import com.google.common.collect.Maps;

import model.Qna;

import java.util.Collection;
import java.util.Map;

public class QnaRepository {
    private static Map<String, Qna> qnas = Maps.newHashMap();

    public static void addQna(Qna qna) {
        qnas.put(qna.getWriter(), qna);
    }

    public static Qna findQnaByUsername(String username) {
        return qnas.get(username);
    }

    public static Collection<Qna> findAllQnas() {
        return qnas.values();
    }

}
