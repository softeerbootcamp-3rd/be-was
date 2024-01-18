package config;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class HTTPRequest{
    private String method;
    private String url;
    private String HTTPType;

    private HashMap<String,String> head;
    private HashMap<String,String> body;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHTTPType() {
        return HTTPType;
    }

    public void setHTTPType(String HTTPType) {
        this.HTTPType = HTTPType;
    }

    public HashMap<String, String> getHead() {
        return head;
    }

    public void setHead(HashMap<String, String> head) {
        this.head = head;
    }

    public HashMap<String, String> getBody() {
        return body;
    }

    public void setBody(HashMap<String, String> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HTTPRequest{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", HTTPType='" + HTTPType + '\'' +
                ", head=" + head +
                ", body=" + body +
                '}';
    }

    public HTTPRequest(BufferedReader br, Logger logger) throws IOException{


        //파싱을 진행
        // ******시간이 남으면 Enum으로 수정할것******
        String line = br.readLine();

        String[] tokens = line.split(" ");
        method = tokens[0];
        url = tokens[1];
        HTTPType = tokens[2];
        logger.debug("header : {}", line);

        head = new HashMap<>();
        body = new HashMap<>();

        line = br.readLine();

        while(line != null && !line.isEmpty() && !line.equals("\n")){
            tokens = line.split(": ");
            if(tokens.length<2){
                br.readLine();
                continue;
            }
            head.put(tokens[0], tokens[1]);
            logger.debug("header : {}", line);
            line = br.readLine();
        }

        while(line != null && !line.isEmpty()) {
            line = br.readLine();
            tokens = line.split("");
            body.put(tokens[0], tokens[1]);
            logger.debug("body : {}", line);
        }

    }

}
