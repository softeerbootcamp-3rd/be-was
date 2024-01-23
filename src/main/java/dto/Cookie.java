package dto;

public class Cookie {
    private String sid;
    private int maxAge;

    public Cookie(String sid, int maxAge) {
        this.sid = sid;
        this.maxAge = maxAge;
    }

    public String getSid() {
        return sid;
    }

    public int getMaxAge() {
        return maxAge;
    }
}
