package webserver.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.GeneralException;
import webserver.request.Request;
import webserver.status.ErrorCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public static Request parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        Request request = parseHeader(br);

        logger.debug(request.toString());

        return request;
    }

    private static Request parseHeader(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        StringTokenizer st = new StringTokenizer(requestLine, " ");

        String method = st.nextToken();
        String path = st.nextToken();
        String protocol = st.nextToken();

        Request request;

        if(path.contains("?")){
            String[] pathSplit = path.split("\\?");
            request = Request.of(method, pathSplit[0], protocol);

            parseParams(request, pathSplit[1]);
        } else{
            request = Request.of(method, path, protocol);
        }

        while(br.ready()) {
            requestLine = br.readLine();

            if(requestLine.isEmpty()){
                if(br.ready()) {
                    int contentLen = Integer.parseInt(request.getHeader("ContentLength"));
                    char[] buf = new char[contentLen];

                    br.read(buf, 0, contentLen);

                    parseBody(request, String.valueOf(buf));
                }
                continue;
            }

            String[] requestLineSplit = requestLine.split(": ");

            String headerName = requestLineSplit[0].replace("-", "");
            String headerValue = requestLineSplit[1];

            request.setHeader(headerName, headerValue);
        }

        return request;
    }

    private static void parseParams(Request request, String plainParam){
        StringTokenizer st;
        String[] paramsSplit = plainParam.split("&");

        for(String param: paramsSplit){
            st = new StringTokenizer(param, "=");

            String key = st.nextToken();
            String value = decode(st.nextToken());

            if(request.existsParam(key)){
                throw new GeneralException(ErrorCode.ILLEGAL_ARGUMENT_ERROR);
            }

            request.setParam(key, value);
        }
    }

    private static void parseBody(Request request, String plainBody){
        StringTokenizer st;
        String[] paramsSplit = plainBody.split("&");

        for(String param: paramsSplit){
            st = new StringTokenizer(param, "=");

            String key = st.nextToken();
            String value = decode(st.nextToken());

            if(request.existsBody(key)){
                throw new GeneralException(ErrorCode.ILLEGAL_ARGUMENT_ERROR);
            }

            request.setBody(key, value);
        }
    }

    private static String decode(String str){
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }
}
