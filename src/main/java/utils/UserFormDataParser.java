package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.RequestHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class UserFormDataParser {
    private static final Logger logger = LoggerFactory.getLogger(UserFormDataParser.class);
    String data;
    public UserFormDataParser(String data) {
        this.data = data;
    }

    public HashMap<String,String> ParseData(){
        HashMap<String, String> formData = new HashMap<>();
        String[] units = data.split("&");
        for(String it : units){
            String[] itSplit = it.split("=");
            String key = itSplit[0];
            String value = URLDecoder.decode(itSplit[1], StandardCharsets.UTF_8);
            formData.put(key, value);
        }
        return formData;
    }
}
