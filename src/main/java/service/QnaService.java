package service;

import db.PostDatabase;
import dto.PostDto;
import model.Post;
import model.User;
import util.HtmlBuilder;

import java.util.Map;

public class QnaService {
    public String writePost(PostDto postDto, User user) {
        String title = postDto.getTitle();
        String contents = postDto.getContents();
        byte[] file = postDto.getFile();
        String fileName = postDto.getFileName();
        String fileContentType = postDto.getFileContentType();

        if (title != null && contents != null) {
            Post post = new Post(user, title, contents, file, fileName, fileContentType);
            return PostDatabase.addPost(post);
        }
        throw new IllegalArgumentException("Invalid Parameters");
    }

    public byte[] printWriteDetailPage(Map<String, String> queryString, User user) {
        String postId = queryString.get("postId");
        Post post = PostDatabase.findPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        return HtmlBuilder.buildWriteDetailPage(post, user);
    }
}
