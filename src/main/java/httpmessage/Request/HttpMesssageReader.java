package httpmessage.Request;

import java.io.*;

import java.io.BufferedReader;

public class HttpMesssageReader {

    private RequestHeader rh;

    public HttpMesssageReader(BufferedReader br) throws IOException {
        rh = new RequestHeader();
        String line = br.readLine();
        separteFirstHeader(line);

        while(!line.isEmpty()){
            line = br.readLine();
            switch(line) {
                case "Accept-Language":
                    this.rh.setAcceptLanguage(line);
                    break;
                case "Accept-Encoding":
                    this.rh.setAcceptEncoding(line);
                    break;
                case "Accept":
                    this.rh.setAccpet(line);
                    break;
                case "Upgrade-Insecure-Requests":
                    this.rh.setUpgradeInsecureRequests(line);
                    break;
            }
        }
        if(rh.getHttpMethod().contains("POST")){

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


