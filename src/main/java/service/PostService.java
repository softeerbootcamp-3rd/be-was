package service;

import db.Database;
import model.Post;

public class PostService {
    public void addPost(Post post) {
        Database.addPost(post);
    }
}
