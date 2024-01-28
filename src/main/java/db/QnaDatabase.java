package db;

import com.google.common.collect.Maps;
import model.Qna;

import java.util.*;

public class QnaDatabase {
    private static Long lastId = 0L;
    private static final Map<Long, Qna> qnas = Maps.newHashMap();

    public static void add(Qna qna) {
        qna.setId(lastId + 1);
        lastId += 1;

        qnas.put(qna.getId(), qna);
    }

    public static Qna findById(Long id) {
        return qnas.get(id);
    }

    public static void deleteById(Long id) {
        qnas.remove(id);
    }

    public static Collection<Qna> findAll() {
        return qnas.values();
    }

    public static int countAll() {
        return qnas.size();
    }

    public static Collection<Qna> getPage(int pageSize, int pageNumber) {
        List<Qna> allQnas = new ArrayList<>(qnas.values());
        Collections.reverse(allQnas);

        int totalQnas = allQnas.size();
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalQnas);

        return allQnas.subList(startIndex, endIndex);
    }
}
