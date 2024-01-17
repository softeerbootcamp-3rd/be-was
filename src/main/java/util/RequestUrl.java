package util;

import java.util.HashMap;

public class RequestUrl {
    private String method = null;
    private String path = null;
    private HashMap<String, String> query = null;

    private RequestUrl() {}

    public static RequestUrl of(String firstLine) {
        RequestUrl requestUrl = new RequestUrl();

        String[] tokens = firstLine.split(" ");
        requestUrl.method = tokens[0];

        String path = tokens[1];
        int indexOfQuestionMark = path.indexOf('?');

        if (indexOfQuestionMark < 0) {
            requestUrl.path = path;
            return requestUrl;
        }

        requestUrl.path = path.substring(0, indexOfQuestionMark);
        requestUrl.query = new HashMap<>();

        String query = path.substring(indexOfQuestionMark + 1);
        String[] params = query.split("&");

        for (String s : params) {
            String[] keyVal = s.split("=");
            requestUrl.query.put(keyVal[0], keyVal[1]);
        }
        return requestUrl;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public HashMap<String, String> getQuery() {
        return query;
    }
}
