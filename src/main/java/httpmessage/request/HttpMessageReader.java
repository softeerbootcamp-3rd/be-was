package httpmessage.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import java.io.*;
import java.io.BufferedReader;
import java.net.URLDecoder;
import java.util.HashMap;

public class HttpMessageReader {

    private RequestHeader rh;
    private Parameter parameter;

    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public HttpMessageReader(BufferedReader br) throws IOException {
        this.rh = new RequestHeader();
        String line = br.readLine();
        separteFirstHeader(line);
        boolean postUrl = false;
        int contentLength = 0;

        while(!line.isEmpty()){
            line = br.readLine();

            if(line.isEmpty()) break;

            String key = line.split(":")[0];
            String value = line.split(":")[1].trim();
            switch(key) {
                case "Accept-Language":
                    this.rh.setAcceptLanguage(value);
                    break;
                case "Accept-Encoding":
                    this.rh.setAcceptEncoding(value);
                    break;
                case "Accept":
                    this.rh.setAccpet(value);
                    break;
                case "Upgrade-Insecure-Requests":
                    this.rh.setUpgradeInsecureRequests(value);
                    break;
                case "Content-Type": // post : 1. application/x-www-form-urlencoded(url형식) 2. application/json
                    if(value.equals("application/x-www-form-urlencoded")){
                        postUrl = true;
                    }
                    break;
                case "Content-Length":
                    contentLength = Integer.parseInt(value);
                    break;
                case "Cookie":
                    this.rh.setCookie(value.split("=")[1]);
                    break;
            }
        }
        if(postUrl) {
            parsingParameter(br,contentLength);
        }

    }
    public void separteFirstHeader(String firstHeader){
        String[] tokens = firstHeader.split(" ");
        String httpMethod = tokens[0];
        String path = tokens[1];
        if(path.equals("/")) path = "/index.html";
        this.rh.setHttpMethod(httpMethod);
        this.rh.setPath(path);
    }

    public void parsingParameter(BufferedReader br, int contentLength) throws IOException {
        StringBuilder bodyJson = new StringBuilder();
        char[] buffer = new char[contentLength];
        HashMap<String,String> map = new HashMap<>();

        br.read(buffer);
        bodyJson.append(buffer);
        String path = bodyJson.toString();

        String[] userInformation = path.split("&");

        for (String s : userInformation) {
            String key = s.split("=")[0];

            if (s.split("=").length == 1) {
                map.put(key, null);
                continue;
            }

            String value = s.split("=")[1];
            String decodeValue = URLDecoder.decode(value, "UTF-8");
            map.put(key, decodeValue);
        }
        this.parameter = new Parameter(map);
        this.rh.setBody(bodyJson);

    }

    public RequestHeader getRequestHeader() {
        return this.rh;
    }
    public Parameter getParameter() {
        return this.parameter;
    }

}


