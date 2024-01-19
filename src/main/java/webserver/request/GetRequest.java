package webserver.request;

import java.util.Map;

public class GetRequest {
    private final String path;
    private final Map<String, String> paramsMap;

    private GetRequest(String path, Map<String, String> paramsMap){
        this.path = path;
        this.paramsMap = paramsMap;
    }

    public static GetRequest of(String path, Map<String, String> paramsMap){
        return new GetRequest(path, paramsMap);
    }

    public static GetRequest of(String path){
        return new GetRequest(path, null);
    }

    public String getPath(){
        return path;
    }

    public Map<String, String> getParamsMap(){
        return paramsMap;
    }
}
