package config;

import controller.Controller;
import dto.GetRequestEnum;
import service.GetService;
import service.PostService;

public class Config {

    public static final GetService getService = new GetService();
    public static final PostService postService = new PostService();

}
