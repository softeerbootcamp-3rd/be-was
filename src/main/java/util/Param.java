package util;

public class Param {
    private String key;
    private String value;

    public Param(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static Param from(String param) {
        String[] split = param.split("=");
        return new Param(split[0], split[1]);
    }
}
