package dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class RequestDto {
    private String method;
    private String url;
    private String contentType;

    private String location = "/index.html";
    private Map<String,String> requestParam = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);


    public RequestDto() {
    }

    public RequestDto(String method, String url) {
        this.method = method;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getLocation() {
        return location;
    }

    public String getContentType() {
        return contentType;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setUrl(String string){
        String[] request = string.split("\\?");
        String url = request[0];
        this.url = url;
        this.setContentType();
        if(request.length>1) {
            String[] params = request[1].split("\\&");

            for (String param : params) {
                String[] data = param.split("\\=");
                this.requestParam.put(data[0], data[1]);
            }
        }
    }

    public void requestInfo(){
        logger.debug("contentType : "+this.contentType);
        logger.debug("method : "+this.method);
        logger.debug("url : "+this.url);
        logger.debug("[ requestParams ]");
        for (Map.Entry<String, String> entry : this.requestParam.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + ":" + value);
        }
    }

    private void setContentType(){
        String fileName = this.url;
        String fileExtension;
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            this.contentType = "text/"+fileName.substring(dotIndex + 1);
        } else {
            this.contentType =  ""; // 확장자가 없을 경우 빈 문자열 반환
        }
    }
}
