package service;

import db.Database;
import db.QnaRepository;
import model.Qna;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    public String getPostList(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Collection<Qna> collection = QnaRepository.findAll();
        List<Qna> posts = new ArrayList<>(collection);

        StringBuilder json = new StringBuilder();
        json.append("[");
        for(int i=0;i<posts.size();i++) {
            Qna post = posts.get(i);
            json.append("{");
            json.append("\"id\":\"").append(post.getId()).append("\",");
            json.append("\"author\":\"").append(post.getWriter().getName()).append("\",");
            json.append("\"title\":\"").append(post.getTitle()).append("\",");
            json.append("\"contents\":\"").append(post.getContent()).append("\",");
            json.append("\"createdAt\":\"").append(post.getCreateAt().format(formatter)).append("\"");
            json.append("}");
            if(i!=posts.size()-1){
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();

    }

}
