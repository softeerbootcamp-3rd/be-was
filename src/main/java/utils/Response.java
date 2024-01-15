package utils;

public class Response {

    public String setPath(String filePath, String type) {
        String basePath = "src/main/resources";
        if (type.equals("text/html")) {
            return basePath + "/templates" + filePath;
        }
        return basePath + "/static" + filePath;
    }

    public String setContentType(String type) {
        if (type.equals("css")) {
            return "text/css";
        }
        if (type.equals("js")) {
            return "text/javascript";
        }
        if (type.contains(".ico")) {
            return "image/x-icon";
        }
        return "text/html";
    }
}
