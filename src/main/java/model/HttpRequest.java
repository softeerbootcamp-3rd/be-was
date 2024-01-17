package model;

public class HttpRequest {
    private String method;
    private String target;
    private String version;

    private HttpHeaders headers;

    public HttpRequest(String method, String target, String version) {
        this.method = method;
        this.target = target;
        this.version = version;
        this.headers = new HttpHeaders();
    }

    public void updateHeaders(String key, String value) {
        headers.addHeader(key, value);
    }

    public String getMethod() {
        return method;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "\n[HttpRequest]\n" +
                "- 일반" +
                "\n요청 URL: " + target +
                "\n요청 메서드: " + method +
                "\n프로토콜: " + version + "\n" +
                "\n- 요청 헤더" +
                "\n" + headers.toString();
    }

}