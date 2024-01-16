package handler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StaticResourceHandler {
    private static final String STATIC_RESOURCE_PATH = "src/main/resources/templates";

    private StaticResourceHandler() {}

    private static class SingletonHelper {
        private static final StaticResourceHandler SINGLETON = new StaticResourceHandler();
    }

    public static StaticResourceHandler getInstance(){
        return SingletonHelper.SINGLETON;
    }

    public byte[] process(String target) throws IOException {
        return Files.readAllBytes(Paths.get(STATIC_RESOURCE_PATH + target));
    }
}
