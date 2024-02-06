package httpmessage.response;

import java.util.HashMap;

public class FilePathContent {

    private static final String TEMPLATE_PATH = "./src/main/resources/templates";
    private static final String STATIC_PATH = "./src/main/resources/static";
    private final HashMap<String, String> extensionToPathMap = initializeExtensionToPathMap();

    private HashMap<String, String> initializeExtensionToPathMap(){
        HashMap<String, String> map = new HashMap<>();
        map.put("html", TEMPLATE_PATH);
        map.put("css", STATIC_PATH);
        map.put("jpg", STATIC_PATH);
        map.put("png", STATIC_PATH);
        map.put("eot", STATIC_PATH);
        map.put("svg", STATIC_PATH);
        map.put("ttf", STATIC_PATH);
        map.put("woff", STATIC_PATH);
        map.put("woff2", STATIC_PATH);
        map.put("ico", STATIC_PATH);
        map.put("js", STATIC_PATH);
        return map;
    }
    public String getFilePath(String path) {
        if(path.contains("?")) {
            path = path.split("\\?")[0];
        }

        String fileExtension = path.split("\\.")[path.split("\\.").length-1];
        return extensionToPathMap.getOrDefault(fileExtension, "404") + path;
    }
}
