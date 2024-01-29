package controller;

import annotation.RequestMapping;
import controller.dto.InputData;
import controller.dto.OutputData;
import db.PostRepository;
import model.Post;

public class PostController implements Controller {

    @RequestMapping(value = "/post/create", method = "POST")
    public String createPost(InputData inputData, OutputData outputData) {
        Post post = new Post(inputData.get("writer"), inputData.get("title"), inputData.get("contents"));
        PostRepository.addPost(post);

        return "redirect:/index";
    }
}
