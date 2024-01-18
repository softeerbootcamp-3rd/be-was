package webserver.parser;

import exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.GetRequest;
import webserver.status.ErrorCode;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
                throw new GeneralException(ErrorCode.ILLEGAL_ARGUMENT_ERROR);
            }

            logger.debug(key + " " + value);
            paramsMap.put(key, value);
        }

        return GetRequest.of(requestPath, paramsMap);
    }

    private static String decode(String str){
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }
}
