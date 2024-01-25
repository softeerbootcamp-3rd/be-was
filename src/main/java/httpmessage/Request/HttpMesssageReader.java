package httpmessage.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.*;

import java.io.BufferedReader;

public class HttpMesssageReader {

    private RequestHeader rh;
    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public HttpMesssageReader(BufferedReader br) throws IOException {
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

            }
        }
        if(postUrl) {
            StringBuilder bodyJson = new StringBuilder();
            char[] buffer = new char[contentLength];
            br.read(buffer);
            bodyJson.append(buffer);
            rh.setBody(bodyJson);
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
    public RequestHeader getRequestHeader() {
        return this.rh;
    }

}


