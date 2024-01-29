package service;

import db.Database;
import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    public void createPost(Post post) {
        Database.addPost(post);
        logger.info(Database.findAllPosts().toString());
    }
}
