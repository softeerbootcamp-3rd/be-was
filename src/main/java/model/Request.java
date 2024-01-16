package model;

public class Request {
    private String method;
    private String url;

    public Request(String method, String url) {
        this.method = method;
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getMimeType() {
        if (url.endsWith(".css")) {
            return "text/css";
        }
        if (url.endsWith(".js")) {
            return "application/javascript";
        }
        // 기타 확장자 처리
        return "text/html";
    }

    public String getFilePath() {
        if (url.endsWith(".html")) {
            return "src/main/resources/templates" + url;
        } else if (!url.isEmpty()) {
            return "src/main/resources/static" + url;
        }
        return "";
    }
}
