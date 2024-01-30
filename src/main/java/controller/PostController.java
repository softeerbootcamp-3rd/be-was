package controller;

import annotation.RequestMapping;
import auth.SessionManager;
import controller.dto.InputData;
import controller.dto.ListMapData;
import controller.dto.OutputData;
import db.PostRepository;
import db.UserRepository;
import model.Comment;
import model.Post;
import view.View;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class PostController implements Controller {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @RequestMapping(value = "/post/create", method = "POST")
    public String createPost(InputData inputData, OutputData outputData) {
        String userId = SessionManager.getUserBySessionId(inputData.getSessionId());
        String userName = UserRepository.findUserById(userId).getName();
        String contents = inputData.get("contents").replace("\r\n", "<br>");

        Post post = new Post(userId, userName, inputData.get("title"), contents);
        PostRepository.addPost(post);

        return "redirect:/index";
    }

    @RequestMapping(value = "/post/show", method = "GET")
    public String showPost(InputData inputData, OutputData outputData) {
        if (inputData.getSessionId() == null) {
            return "redirect:/user/login.html";
        }

        Long postId = Long.parseLong(inputData.get("postId"));
        Post post = PostRepository.findByPostId(postId);

        View view = outputData.getView();
        setPostInfo(view, post);

        ListMapData listMapData = new ListMapData();
        for (Comment comment : post.getCommentList()) {
            Map<String, String> c = setCommentInfo(comment);
            listMapData.putMap(c);
        }
        view.set("userId", SessionManager.getUserBySessionId(inputData.getSessionId()));
        view.set("comments", listMapData);
        view.set("commentNumber", listMapData.getListSize().toString());
        return "/qna/show.html";
    }

    @RequestMapping(value = "/post/form", method = "GET")
    public String getPostForm(InputData inputData, OutputData outputData) {
        String sessionId = inputData.getSessionId();
        if (sessionId == null)
            return "/user/login.html";
        return "/qna/form.html";
    }

    @RequestMapping(value = "/post/comment", method = "POST")
    public String createComment(InputData inputData, OutputData outputData) {

        Long postId = Long.parseLong(inputData.get("postId"));
        String userId =SessionManager.getUserBySessionId(inputData.getSessionId());
        String content = inputData.get("content");
        String writer = UserRepository.findUserById(userId).getName();
        PostRepository.addComment(postId, new Comment(userId, writer, content));

        return "redirect:/post/show?postId="+postId;
    }

    private void setPostInfo(View view, Post post) {
        view.set("writer", post.getWriter());
        view.set("contents", post.getContents());
        view.set("title", post.getTitle());
        view.set("createdAt", post.getCreatedAt().format(formatter));
        view.set("postId", post.getPostId().toString());
    }

    private Map<String, String> setCommentInfo(Comment comment) {
        Map<String, String> c = new HashMap<>();
        c.put("userId", comment.getUserId());
        c.put("writer", comment.getWriter());
        c.put("content", comment.getContent());
        c.put("createdAt", comment.getCreatedAt().format(formatter));
        return c;
    }
}
