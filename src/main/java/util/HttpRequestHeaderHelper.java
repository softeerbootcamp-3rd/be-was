package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.http.HttpHeaders;

public class HttpRequestHeaderHelper {
    private static final String END = "";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String HOST = "Host:";
    private static final String CONNECTION = "Connection:";
    private static final String ACCEPT = "Accept:";
    private static final String NEW_LINE = "\n";

    public static String getRequestHeader(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, CHARSET_NAME));
        StringBuilder request = new StringBuilder();

        String line = br.readLine();
        request.append(NEW_LINE).append(line).append(NEW_LINE);

        while(!line.equals(END)){
            line = br.readLine();
            if(line.startsWith(HOST) || line.startsWith(CONNECTION) || line.startsWith(ACCEPT)){
                request.append(line).append(NEW_LINE);
            }
        }

        return request.toString();
    }
}
