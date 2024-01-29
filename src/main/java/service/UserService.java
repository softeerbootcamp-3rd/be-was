package service;

import db.Database;
import dto.request.UserSignUpDto;
import model.User;

import java.util.Collection;

public class UserService {
    public static void signUp(UserSignUpDto userSignUpDto) {
        User user = new User(userSignUpDto.getUserId(), userSignUpDto.getPassword(), userSignUpDto.getName(), userSignUpDto.getEmail());
        if(Database.findUserById(user.getUserId()) != null){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Database.addUser(user);
    }

    public static String login(String id, String password) {
        User findUser = Database.findUserById(id);

        if (findUser == null || !findUser.getPassword().equals(password)) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        return id;
    }

    public static String userList() {
        Collection<User> users = Database.findAll();

        return getUserList(users);
    }

    private static String getUserList(Collection<User> users) {
        StringBuilder sb = new StringBuilder();
        int pos = 1;

        for (User user : users) {
            sb.append("<tr>\n")
                    .append("<th scope=\"row\">").append(pos++).append("</th>")
                    .append("<td>").append(user.getUserId()).append("</td>")
                    .append("<td>").append(user.getName()).append("</td>")
                    .append("<td>").append(user.getEmail()).append("</td>")
                    .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n")
                    .append("</tr>\n");
        }

        return sb.toString();
    }
}
