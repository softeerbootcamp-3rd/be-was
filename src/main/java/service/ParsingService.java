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
        String line = br.readLine();

        //logger.debug(line);
        rh.setGeneralHeader(line);
        while(!line.isEmpty()){
            line = br.readLine();
            //logger.debug(line);

            if(line.contains("Accept")) {
                if (line.contains("Language")) rh.setAcceptLanguage(line);
                else if(line.contains("Encoding")) rh.setAcceptEncoding(line);
                else rh.setAccpet(line);
            }

            else if(line.contains("Upgrade-Insecure-Requests")) rh.setUpgrade_insecure_requests(line);
        }

    }

}
