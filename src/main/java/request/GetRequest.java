package request;

import java.util.Map;

public class GetRequest {
    private final String path;
    private final Map<String, String> paramsMap;

    public GetRequest(String path, Map<String, String> paramsMap){
        this.path = path;
        this.paramsMap = paramsMap;
    }

    public String getPath(){
        return path;
    }

    public Map<String, String> getParamsMap(){
        return paramsMap;
    }
}
