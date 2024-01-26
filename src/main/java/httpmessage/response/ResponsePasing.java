package httpmessage.response;

import java.util.HashMap;

public class ResponsePasing {

    private static final String TEMPLATE_PATH = "./src/main/resources/templates";
    private static final String STATIC_PATH = "./src/main/resources/static";
    private final HashMap<String, String> extensionToPathMap = initializeExtensionToPathMap();
    private final HashMap<Integer, String> httpStatus = initializeHttpStatus();

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
    private HashMap<Integer, String> initializeHttpStatus(){
        HashMap<Integer, String> map = new HashMap<>();
        map.put(200, "OK");
        map.put(201, "Created");
        map.put(302, "Moved Temporarily");
        map.put(400, "Bad Request");
        map.put(404, "Not Found");
        return map;
    }
    public String getUrl(String path) {
        String fileExtension = path.split("\\.")[path.split("\\.").length-1];
        return extensionToPathMap.getOrDefault(fileExtension, "404") + path;
    }

    public String getSatusCode(int code) {
        return httpStatus.getOrDefault(code, "해당 코드는 없습니다");
    }

}
