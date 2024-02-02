package db;

import model.Qna;
import model.User;

import java.util.*;

public class QnaDatabase {


    private static Map<String, Qna> qnas = Collections.synchronizedMap(new HashMap<>());

    public static void addQnas(Qna qna) {
        qnas.put(qna.getId(), qna);
    }

    public static Qna findQnaById(String qnaId) {
        return qnas.get(qnaId);
    }

    public static List<Qna> findAll() {
        return new ArrayList<>(qnas.values());
    }

}
