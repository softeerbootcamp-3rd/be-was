package data;

public class CookieData {
    private final String sid;
    private final int maxAge;

    public CookieData(String sid, int maxAge) {
        this.sid = sid;
        this.maxAge = maxAge;
    }


    public String getSid() {
        return sid;
    }
    public int getMaxAge() {
        return maxAge;
    }

    @Override
    public String toString() {
        return "sid=" + getSid() + "; Max-Age=" + getMaxAge() + ";";
    }
}
