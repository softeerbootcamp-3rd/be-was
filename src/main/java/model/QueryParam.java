package model;

public class QueryParam {
    private String key;
    private String value;

    public QueryParam(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static QueryParam from(String param) {
        String[] split = param.split("=");
        return new QueryParam(split[0], split[1]);
    }
}
