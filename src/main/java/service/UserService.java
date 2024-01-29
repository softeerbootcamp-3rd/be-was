package service;

import db.Database;
import db.QnaRepository;
import model.Qna;
import model.User;

import java.util.Optional;

public class UserService {
    private final Database database = Database.getInstance();
    private final QnaRepository qnaRepository = QnaRepository.getInstance();
    private static final UserService instance = new UserService();

    public static UserService getInstance(){
        return instance;
    }

    public void signUp(User user) {
        database.addUser(user);
    }

    public Optional<User> login(String userId, String password) {
        return Optional.ofNullable(database.findUserById(userId))
                .filter(user -> user.getPassword().equals(password));
    }

    public String userInfo(Long postId){
        Qna post = qnaRepository.findQnaById(postId);
        User author = post.getWriter();
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"name\":\"").append(author.getName()).append("\",");
        json.append("\"email\":\"").append(author.getEmail()).append("\"");
        json.append("}");
        return json.toString();
    }
}
