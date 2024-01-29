package controller;

import annotation.RequestMapping;
import controller.dto.InputData;
import controller.dto.OutputData;
import db.PostRepository;
import model.Post;
import view.View;

public class PostController implements Controller {

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

        return "/qna/show.html";
    }
}
