package dto;

public class Cookie {
    private String sid;
    private int maxAge;
    private String path;

    public Cookie(String sid, int maxAge, String path) {
        this.sid = sid;
        this.maxAge = maxAge;
        this.path = path;
    }

    public String getSid() {
        return sid;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public String getPath() {
        return path;
    }
}
