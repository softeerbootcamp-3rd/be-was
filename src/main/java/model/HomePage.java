package model;

import java.util.ArrayList;
import java.util.List;

public class HomePage {
    private List<Post> recentPosts;

    public HomePage() {
        this.recentPosts = new ArrayList<>();
    }

    public void addPost(Post post) {
        recentPosts.add(0, post);
        if (recentPosts.size() > 10) {
            recentPosts.remove(10);
        }
    }

    public List<Post> getRecentPosts() {
        return recentPosts;
    }
}
