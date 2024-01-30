package controller;

import annotation.RequestMapping;
import auth.SessionManager;
import controller.dto.InputData;
import controller.dto.ListMapData;
import controller.dto.OutputData;
import db.PostRepository;
import model.Post;
import view.View;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class IndexController implements Controller {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @RequestMapping(value = "/index", method = "GET")
    public String indexPage(InputData inputData, OutputData outputData) {
        View view = outputData.getView();
        ListMapData listMapData = new ListMapData();
        for (Post post : PostRepository.findAll()) {
            listMapData.putMap(setPostInfo(post));
        }
        view.set("posts", listMapData);
        view.set("userId", "a");
        return "/index.html";
    }

    @RequestMapping(value = "/index/search", method = "GET")
    public String searchPage(InputData inputData, OutputData outputData) {
        String findThis = inputData.get("srch-term");

        ListMapData listMapData = new ListMapData();
        for (Post post : PostRepository.findAll()) {
            if (post.getTitle().contains(findThis)) {
                listMapData.putMap(setPostInfo(post));
            }
        }
        outputData.getView().set("posts", listMapData);

        return "/index.html";
    }

    private Map<String, String> setPostInfo(Post post) {
        Map<String, String> p = new HashMap<>();
        p.put("postId", post.getPostId().toString());
        p.put("writer", post.getWriter());
        p.put("title", post.getTitle());
        p.put("createdAt", post.getCreatedAt().format(formatter));
        return p;
    }
}
