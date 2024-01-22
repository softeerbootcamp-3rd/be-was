package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ViewResolver;

import java.util.HashMap;
import java.util.Map;

public class Request {
    //상수 및 클래스 변수
    private static final Logger logger = LoggerFactory.getLogger(Request.class);
    //인스턴스 변수
    private String method;
    private String url;
    private String location = "";
    private Map<String,String> requestParam = new HashMap<>();
    //생성자
    public Request() {
    }

    public Request(String method, String url) {
        this.method = method;
        this.setUrl(url);
    }
    //메서드
    public String getUrl() {
        return url;
    }
    public String getLocation() {return location;}
    public Map<String, String> getRequestParam() {return requestParam;}

    public String getMethod() {return method;}

    public void setMethod(String method) {this.method = method;}
    public void setRequestParam(String[] params){
        for (String param : params) {
            String[] data = param.split("\\=");
            if(data.length!=2){
                this.requestParam.put(data[0], "");
            }
            else{
                this.requestParam.put(data[0], data[1]);
            }
        }
    }

    public void setUrl(String string){
        String[] request = string.split("\\?");
        url = request[0];
        if(ViewResolver.isTemplate(url)||ViewResolver.isStatic(url)){
            requestParam.put("content",url);
        }
        if(request.length>1) {
            setRequestParam(request[1].split("\\&"));
        }
    }

    public void requestInfo(){
        logger.debug("method : "+this.method);
        logger.debug(new StringBuilder("url : ").append(url).toString());
        logger.debug(new StringBuilder("[ requestParams ]").toString());
        for (Map.Entry<String, String> entry : this.requestParam.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
        }
    }


}
