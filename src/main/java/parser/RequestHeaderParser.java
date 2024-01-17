package parser;

import webserver.header.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.StringTokenizer;

public class RequestHeaderParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestHeaderParser.class);

    public static RequestHeader parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        RequestHeader requestHeader = parseHeader(br);

        logger.debug(requestHeader.toString());

        return requestHeader;
    }

    private static RequestHeader parseHeader(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        StringTokenizer st = new StringTokenizer(requestLine, " ");

        String method = st.nextToken();
        String path = st.nextToken();
        String protocol = st.nextToken();

        RequestHeader requestHeader = RequestHeader.of(method, path, protocol);

        while(!(requestLine = br.readLine()).isEmpty()){
            String[] requestLineSplit = requestLine.split(": ");

            String headerName = requestLineSplit[0].replace("-", "");
            String headerValue = requestLineSplit[1];

            setHeaderValue(requestHeader, headerName, headerValue);
        }

        return requestHeader;
    }

    private static void setHeaderValue(RequestHeader requestHeader, String fieldName, String fieldValue){
        Class<RequestHeader> requestHeaderClass = RequestHeader.class;

        try{
            Field field = requestHeaderClass.getDeclaredField(fieldName);
            field.setAccessible(true);

            field.set(requestHeader, fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
