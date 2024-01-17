package parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.GetRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class GetRequestParser {
    private static final Logger logger = LoggerFactory.getLogger(GetRequestParser.class);

    public static GetRequest parse(String path){
        if(!path.contains("?")){
            return GetRequest.of(path);
        }

        StringTokenizer st = new StringTokenizer(path, "?");

        String requestPath = st.nextToken();
        String params = st.nextToken();

        String[] paramsSplit = params.split("&");

        Map<String, String> paramsMap = new LinkedHashMap<>();

        for(String param: paramsSplit){
            st = new StringTokenizer(param, "=");

            String key = st.nextToken();
            String value = decode(st.nextToken());

            if(paramsMap.containsKey(key)){
                throw new IllegalArgumentException("중복된 파라미터 요청");
            }

            logger.debug(key + " " + value);
            paramsMap.put(key, value);
        }

        return GetRequest.of(requestPath, paramsMap);
    }

    private static String decode(String str){
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return null;
        }
    }
}
