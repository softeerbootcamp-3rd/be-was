package utils;

public class Response {

    public String getPath(String filePath, String type) {
        String basePath = "src/main/resources";
        if (type.equals("text/html")) {
            return basePath + "/templates" + filePath;
        }
        return basePath + "/static" + filePath;
    }

    public String getContentType(String file) {
        if (file.endsWith(".css")) {
            return "text/css";
        }
        if (file.endsWith(".js")) {
            return "text/javascript";
        }
        if (file.endsWith(".ico")) {
            return "image/x-icon";
        }
        if (file.endsWith(".ttf")) {
            return "font/ttf";
        }
        if (file.endsWith(".woff")) {
            return "font/woff";
        }
        return "text/html";
    }
}
