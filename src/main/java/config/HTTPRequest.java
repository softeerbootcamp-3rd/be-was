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

        // Body는 EOF가 없기 때문에 헤더의 Content-Length를 이용
        if(this.head.get("Content-Length")!=null) {
            int length = Integer.parseInt(this.head.get("Content-Length"));
            char[] bodyChars = new char[length];

            //char형 배열에 저장 후 String형으로 변환
            br.read(bodyChars, 0, length);
            String bodyString = String.valueOf(bodyChars);

            // body에서 받은 정보는 key - value 값으로 저장
            // **수정할 것** 받게 되는 body형식이 각자 다를 수 있으므로 이 기능은 컨트롤러에서 구현해야함
            for(String str:bodyString.split("&")){
                tokens = str.split("=");
                if(tokens.length<2)
                    continue;
                body.put(tokens[0],tokens[1]);
                logger.debug("body : {}", tokens[0] + " " +tokens[1]);
            }
        }
    }
}
