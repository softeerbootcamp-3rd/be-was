package webserver.request;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private final String method;
    private final String path;
    private final String protocol;
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final Map<String, String> body;

    protected Request(String method, String path, String protocol){
        this.method = method;
        this.path = path;
        this.protocol = protocol;
        this.headers = new HashMap<>();
        this.params = new HashMap<>();
        this.body = new HashMap<>();
    }

    public static Request of(String method, String path, String protocol){
        return new Request(method, path, protocol);
    }

    public String getPath(){
        return path;
    }

    public String getMethod(){
        return method;
    }

    public String getParam(String key){
        return params.getOrDefault(key, null);
    }

    public String getBody(String key){
        return body.getOrDefault(key, null);
    }

    public String getHeader(String key){
        return headers.getOrDefault(key, null);
    }

    public void setHeader(String key, String value){
        headers.put(key, value);
    }

    public void setParam(String key, String value){
        params.put(key, value);
    }

    public void setBody(String key, String value){
        body.put(key, value);
    }

    public boolean existsParam(String key){
        return params.containsKey(key);
    }

    public boolean existsBody(String key){
        return body.containsKey(key);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("\n요청 메소드: %s\n" +
                "요청한 자원의 경로: %s\n" +
                "프로토콜: %s\n", method, path, protocol));

        for(String key: headers.keySet()){
            sb.append(String.format("%s: %s", key, headers.get(key))).append("\n");
        }

        for(String key: params.keySet()){
            sb.append(String.format("%s: %s", key, params.get(key))).append("\n");
        }

        for(String key: body.keySet()){
            sb.append(String.format("%s: %s", key, body.get(key))).append("\n");
        }

        return sb.toString();
    }
}
