package config;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class HTTPRequest{
    private String method;
    private String url;
    private String HTTPType;

    private HashMap<String,String> head;

    private HashMap<String,String> body;

    public String getMethod() {
        return method;
    }
    public String getUrl() {
        return url;
    }
    public String getHTTPType() {
        return HTTPType;
    }
    public HashMap<String, String> getHead() {
        return head;
    }
    public HashMap<String, String> getBody() {
        return body;
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

        while(line != null && !line.isEmpty() && !line.equals("\r\n")){
            tokens = line.split(": ");
            if(tokens.length<2){
                br.readLine();
                continue;
            }
            head.put(tokens[0], tokens[1]);
            logger.debug("header : {}", line);
            line = br.readLine();
        }

        if(this.head.get("Content-Length")!=null) {
            int length = Integer.parseInt(this.head.get("Content-Length"));
            char[] bodyChars = new char[length];
            br.read(bodyChars, 0, length);
            String bodyString = String.valueOf(bodyChars);
            System.out.println("[["+bodyString+"]]");
            for(String str:bodyString.split("&")){
                tokens = str.split("=");
                if(tokens.length<2)
                    continue;
                body.put(tokens[0],tokens[1]);
                logger.debug("body : {}", tokens[0] + " " +tokens[1]);
            }

        }
//        while(line != null && !line.isEmpty()) {
//            line = br.readLine();
//            tokens = line.split(": ");
//            body.put(tokens[0], tokens[1]);
//
//        }

    }
    public void print(){
        for(Map.Entry<String,String> entry : head.entrySet()){
            System.out.println(entry.getKey()+": "+entry.getValue());
        }
        System.out.println();
        for(Map.Entry<String,String> entry : body.entrySet()){
            System.out.println(entry.getKey()+": "+entry.getValue());
        }
    }

}
