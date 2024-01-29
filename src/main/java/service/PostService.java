package service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class PostService {

    public void createPost(String name, Map<String, String> params, LocalDateTime time) {
        System.out.println(name);
        for (String key : params.keySet()) {
            System.out.println(key + " : " + params.get(key));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        System.out.println(time.format(formatter));
    }
}
