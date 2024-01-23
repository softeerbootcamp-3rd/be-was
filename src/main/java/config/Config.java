package config;

import service.GetService;
import service.PostService;

public class Config {

    public static final PostService postService = new PostService();
    public static final GetService getService = new GetService();
}
