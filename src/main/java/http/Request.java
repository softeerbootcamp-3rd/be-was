package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.FrontController;
import webserver.ViewResolver;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String method;
    private String url;

    private String location = "/index.html";
    private Map<String,String> requestParam = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
    private final ViewResolver viewResolver = new ViewResolver();

    public Request() {
    }

    public Request(String method, String url) {
        this.method = method;
        this.setUrl(url);
    }

    public String getUrl() {
        return url;
    }

    public String getLocation() {
        return location;
    }



    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getRequestParam() {
        return requestParam;
    }

    public void setUrl(String string){
        String[] request = string.split("\\?");
        String url = request[0];
        this.url = url;

        if(viewResolver.isTemplate(url)||viewResolver.isStatic(url)){
            requestParam.put("content",url);
        }

        if(request.length>1) {
            String[] params = request[1].split("\\&");

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
    }

    public void requestInfo(){
        logger.debug("method : "+this.method);
        logger.debug("url : "+this.url);
        logger.debug("[ requestParams ]");
        for (Map.Entry<String, String> entry : this.requestParam.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
        }
    }


}
