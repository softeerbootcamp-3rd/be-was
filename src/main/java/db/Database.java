package db;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import model.Qna;
import model.User;

import java.util.Collection;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Database {
    private static Long qnaId = 0L;

    //Maps는 생성할 때 편하게 해주는것, 기능상 차이는 없음
    private static Map<String, User> users = new ConcurrentHashMap<>();
    private static Map<Long, Qna> qnas = new ConcurrentHashMap<>();

    private static Deque<Qna> recentQnas = new ConcurrentLinkedDeque<>();
    public static void addQna(Qna qna){
        qnas.put(qnaId++, qna);
        recentQnas.addFirst(qna);

        while(recentQnas.size() > 100)
            recentQnas.removeLast();


    }
    public static Collection<Qna> getRecentQnas(){return recentQnas;}
    public static void addUser(User user) { users.put(user.getUserId(), user); }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
