package service;

import db.BoardDatabase;
import exception.PostException;
import exception.UserException;
import model.Post;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.StringUtils.decode;

public class BoardService {
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    public void write(User writer, String title, String contents) {
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
        logger.debug(post.toString());
    }

}
