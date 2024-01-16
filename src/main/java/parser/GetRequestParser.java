package parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.GetRequest;
import webserver.RequestHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class GetRequestParser {
    private static final Logger logger = LoggerFactory.getLogger(GetRequestParser.class);

    public static GetRequest parse(String path){
        StringTokenizer st = new StringTokenizer(path, "?");

        String requestPath = st.nextToken();
        String params = st.nextToken();

        String[] paramsSplit = params.split("&");

        Map<String, String> paramsMap = new LinkedHashMap<>();

        for(String param: paramsSplit){
            st = new StringTokenizer(param, "=");

            String key = st.nextToken();
            String value = st.nextToken();

            if(paramsMap.containsKey(key)){
                throw new IllegalArgumentException("중복된 파라미터 요청");
            }

            logger.debug(key + " " + value);
            paramsMap.put(key, value);
        }

        return new GetRequest(requestPath, paramsMap);
    }
}
