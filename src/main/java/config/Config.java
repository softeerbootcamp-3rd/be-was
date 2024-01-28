package config;

import service.GetService;
import service.PostService;

public class Config {

    public static final PostService httpPostService = new PostService();
    public static final GetService httpGetService = new GetService();
    public static Long postId = 0L;
}
