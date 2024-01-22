package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.GetService;
import service.PostService;
import webserver.RequestHandler;

public class Config {

    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    public static final GetService getService = new GetService();
    public static final PostService postService = new PostService();

}
