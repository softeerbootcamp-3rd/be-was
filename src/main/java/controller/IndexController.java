package controller;

import annotation.RequestMapping;
import controller.dto.InputData;
import controller.dto.ListMapData;
import controller.dto.OutputData;
import db.PostRepository;
import model.Post;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class IndexController implements Controller {

    @RequestMapping(value = "/index", method = "GET")
    public String indexPage(InputData inputData, OutputData outputData) {

        ListMapData listMapData = new ListMapData();
        for (Post post : PostRepository.findAll()) {
            Map<String, String> p = new HashMap<>();
            p.put("postId", post.getPostId().toString());
            p.put("writer", post.getWriter());
            p.put("title", post.getTitle());
            listMapData.putMap(p);
        }
        outputData.getView().set("posts", listMapData);

        return "/index.html";
    }
}
