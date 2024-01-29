package service;

import db.Database;
import db.QnaRepository;
import model.Qna;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Model;
import webserver.adaptor.RequestHandlerAdapter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QnaService {
    private static final Logger logger = LoggerFactory.getLogger(QnaService.class);

    private final QnaRepository qnaRepository = QnaRepository.getInstance();
    private static final QnaService instance = new QnaService();

    public static QnaService getInstance(){
        return instance;
    }

    public void create(Qna qna) {
        qnaRepository.addQna(qna);
    }

    public String getPostList(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Collection<Qna> collection = qnaRepository.findAll();
        List<Qna> posts = new ArrayList<>(collection);

        StringBuilder json = new StringBuilder();
        json.append("[");
        for(int i=0;i<posts.size();i++) {
            Qna post = posts.get(i);
            json.append("{");
            json.append("\"id\":\"").append(post.getId()).append("\",");
            json.append("\"author\":\"").append(post.getWriter().getName()).append("\",");
            json.append("\"title\":\"").append(post.getTitle()).append("\",");
            json.append("\"createdAt\":\"").append(post.getCreateAt().format(formatter)).append("\"");
            json.append("}");
            if(i!=posts.size()-1){
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();

    }

    public String getPost(Long id){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Qna post = qnaRepository.findQnaById(id);
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"author\":\"").append(post.getWriter().getName()).append("\",");
        json.append("\"title\":\"").append(post.getTitle()).append("\",");
        json.append("\"contents\":[");
        List<String> contents = post.getContent();
        for(int i=0;i<contents.size();i++){
            String content = contents.get(i);
            json.append("\"").append(content).append("\"");
            if(i!=contents.size()-1){
                json.append(",");
            }
        }
        json.append("],");
        json.append("\"createdAt\":\"").append(post.getCreateAt().format(formatter)).append("\"");
        json.append("}");

        return json.toString();
    }

}
