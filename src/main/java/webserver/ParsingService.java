package webserver;

import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.RequestHeader;
import java.io.BufferedReader;

public class ParsingService {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);


    public ParsingService(BufferedReader br, RequestHeader rh) throws IOException {
        String line = br.readLine();
        rh.setGeneralHeader(line);
        while(!line.equals("")){
            line = br.readLine();

            if(line.contains("Accept")) {
                if (line.contains("Language")) rh.setAccept_language(line);
                else if(line.contains("Encoding")) rh.setAccept_encoding(line);
                else rh.setAccpet(line);
            }
            else if(line.contains("Upgrade-Insecure-Requests")) rh.setUpgrade_insecure_requests(line);

            else logger.debug("request header:{}",line);
        }

    }

}
