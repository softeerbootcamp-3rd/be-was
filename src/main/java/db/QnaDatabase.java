package db;

import com.google.common.collect.Maps;
import model.Qna;

import java.util.Collection;
import java.util.Map;

public class QnaDatabase {
    private static Long lastId = 1L;
    private static final Map<Long, Qna> qnas = Maps.newHashMap();

    public static void add(Qna qna) {
        qna.setId(lastId + 1);
        lastId += 1;

        qnas.put(qna.getId(), qna);
    }

    public static Qna findById(Long id) {
        return qnas.get(id);
    }

    public static Collection<Qna> findAll() {
        return qnas.values();
    }
}
