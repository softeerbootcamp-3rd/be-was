package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FileLoader {
    private static final String STATIC_PATH = "src/main/resources/static";
    private static final Map<String, String> TYPE_PATH = new HashMap<>();
    private static final String HTML_PATH = "src/main/resources/templates";

    public FileLoader(){
        this.setStaticPath();
    }
    private void setStaticPath(){
        TYPE_PATH.put("css", STATIC_PATH);
        TYPE_PATH.put("eot", STATIC_PATH);
        TYPE_PATH.put("ttf", STATIC_PATH);
        TYPE_PATH.put("woff", STATIC_PATH);
        TYPE_PATH.put("woff2", STATIC_PATH);
        TYPE_PATH.put("svg", STATIC_PATH);
        TYPE_PATH.put("png", STATIC_PATH);
        TYPE_PATH.put("js", STATIC_PATH);
        TYPE_PATH.put("html", HTML_PATH);
        TYPE_PATH.put("ico", STATIC_PATH);
    }

    public byte[] loadFileContent(String requestPath, String mimeType) throws IOException {
        return Files.readAllBytes(new File(TYPE_PATH.get(mimeType) + requestPath).toPath());
    }



}
