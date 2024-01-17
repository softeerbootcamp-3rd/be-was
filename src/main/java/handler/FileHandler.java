package handler;

import model.HttpRequest;
import model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpUtil;
import model.HttpStatusCode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.TreeMap;

public class FileHandler {
    private static final Logger logger = LoggerFactory.getLogger(FileHandler.class);

    public static FileHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final FileHandler INSTANCE = new FileHandler();
    }

    public byte[] getFileAsBody(String url) throws IOException {
        String contentType = HttpUtil.getContentTypeFromUrl(url);
        if (contentType.equals("text/html")) {
            return Files.readAllBytes(
                    new File("src/main/resources/templates" + url).toPath());
        }
        else {
            return Files.readAllBytes(
                    new File("src/main/resources/static" + url).toPath());
        }
    }

    public HttpResponse handle(HttpRequest request) throws IOException {
        String contentType = HttpUtil.getContentTypeFromUrl(request.getUrl());
        byte[] body = getFileAsBody(request.getUrl());

        Map<String, String> properties = new TreeMap<>();
        properties.put("Content-Type", contentType);
        properties.put("Content-Length", String.valueOf(body.length));

        return HttpResponse.of(HttpStatusCode.OK, body, properties);
    }
}
