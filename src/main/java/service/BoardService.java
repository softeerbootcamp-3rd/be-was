package service;

import db.BoardDatabase;
import db.UserDatabase;
import exception.PostException;
import exception.UserException;
import exception.WebException;
import model.Post;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.http.HttpStatus;

import static util.StringUtils.decode;

public class BoardService {
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    public Long write(User writer, String title, String contents) {
        if (writer == null)
            throw new PostException(PostException.NULL_WRITER);
        if (title == null)
            throw new PostException(PostException.NULL_TITLE);
        if (contents == null)
            throw new PostException(PostException.NULL_CONTENTS);

        title = decode(title);
        contents = decode(contents);

        Post post = new Post(writer, title, contents);
        BoardDatabase.addPost(post);
        logger.debug("Created: " + post.toString());
        return post.getPostId();
    }

    public Long update(Long postId, String title, String contents) {
        title = decode(title);
        contents = decode(contents);

        Post post = BoardDatabase.findPostById(postId);
        post.update(title, contents);
        logger.debug("Updated: " + post.toString());
        return post.getPostId();
    }

    public Long comment(Long postId, User writer, String body) {
        body = decode(body);

        Post post = BoardDatabase.findPostById(postId);
        return post.addComment(writer, body);
    }

    public Post getPostById(Long postId) {
        return BoardDatabase.findPostById(postId);
    }

    public void delete(Long postId, User loggedInUser) {
        Post post = BoardDatabase.findPostById(postId);
        if (loggedInUser == null || !loggedInUser.equals(post.getWriter()))
            throw new WebException(HttpStatus.UNAUTHORIZED);

        BoardDatabase.removePost(postId);
    }

    public void deleteComment(Long postId, Long commentId) {
        Post post = BoardDatabase.findPostById(postId);

        post.deleteComment(commentId);
    }

}
