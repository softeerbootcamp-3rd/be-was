package service;

import java.io.*;
import model.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServer;

import java.io.BufferedReader;

public class ParsingService {

    public ParsingService(BufferedReader br, RequestHeader rh) throws IOException {
        final Logger logger = LoggerFactory.getLogger(WebServer.class);

        headerParsing(br, rh);
        separatedGeneralHeader(rh);
    }

    public void headerParsing(BufferedReader br, RequestHeader rh) throws IOException {
        String line = br.readLine();
        rh.setGeneralHeader(line);

        while(!line.isEmpty()){
            line = br.readLine();
            switch(line) {
                case "Accept-Language":
                    rh.setAcceptLanguage(line);
                    break;
                case "Accept-Encoding":
                    rh.setAcceptEncoding(line);
                    break;
                case "Accept":
                    rh.setAccpet(line);
                    break;
                case "Upgrade-Insecure-Requests":
                    rh.setUpgradeInsecureRequests(line);
                    break;
            }
        }
    }

    public void separatedGeneralHeader(RequestHeader rh){
        String[] tokens = rh.getGeneralHeader().split(" ");
        String httpMethod = tokens[0];
        String path = tokens[1];

        if(path.equals("/")) path = "/index.html";

        rh.setHttpMethod(httpMethod);
        rh.setPath(path);
    }
}


