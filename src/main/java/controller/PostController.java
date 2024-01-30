package controller;

import annotation.RequestMapping;
import controller.dto.InputData;
import controller.dto.OutputData;
import db.PostRepository;
import model.Post;
import view.View;

import java.time.format.DateTimeFormatter;

public class PostController implements Controller {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @RequestMapping(value = "/post/create", method = "POST")
    public String createPost(InputData inputData, OutputData outputData) {
        Post post = new Post(inputData.get("writer"), inputData.get("title"), inputData.get("contents"));
        PostRepository.addPost(post);

        return "redirect:/index";
    }

    @RequestMapping(value = "/post/show", method = "GET")
    public String showPost(InputData inputData, OutputData outputData) {
        Long postId = Long.parseLong(inputData.get("postId"));
        Post post = PostRepository.findByPostId(postId);

        View view = outputData.getView();
        view.set("writer", post.getWriter());
        view.set("contents", post.getContents());
        view.set("title", post.getTitle());
        view.set("createdAt", post.getCreatedAt().format(formatter));

        return "/qna/show.html";
    }

    @RequestMapping(value = "/post/form", method = "GET")
    public String getPostForm(InputData inputData, OutputData outputData) {
        String sessionId = inputData.getSessionId();
        if(sessionId==null)
            return "/user/login.html";
        return "/qna/form.html";
    }
}
