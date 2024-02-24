package util.html;

import database.UserRepository;
import entity.User;
import model.SharedData;

import java.util.ArrayList;
import java.util.List;

public class UserHtml {
    public static String userName(String template) {
        return template.replace("<!--user-name-->", SharedData.requestUser.get().getName());
    }

    public static String userList(String template) {
        StringBuilder sb = new StringBuilder();
        List<User> userList = new ArrayList<>(UserRepository.findAll());
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            sb.append(template.replace("<!--order-->", String.valueOf(i + 1))
                    .replace("<!--user-id-->", user.getUserId())
                    .replace("<!--user-name-->", user.getName())
                    .replace("<!--user-email-->", user.getEmail()));
        }
        return sb.toString();
    }

    public static String profile(String template) {
        User user = SharedData.requestUser.get();
        return template.replace("<!--profile-name-->", user.getName())
                .replace("<!--profile-email-->", user.getEmail());
    }
}
