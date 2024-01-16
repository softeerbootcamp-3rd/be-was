package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public final class RequestParser {
    private static Map<String, String > headers = new ConcurrentHashMap<String, String>();

    public static void setHeaders(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        //첫 번째 라인에서 메서드, 경로 추출
        String firstLine = reader.readLine();
        if(firstLine == null || firstLine.isEmpty()){
            throw new IllegalArgumentException("Invalid HTTP request");
        }

        String[] firstLineParts = firstLine.split(" ");
        headers.put("Method", firstLineParts[0]);
        headers.put("Path", firstLineParts[1]);

        //다른 헤더 추출
        String line;
        while((line = reader.readLine())!=null && !line.isEmpty()){
            String[] headerParts = line.split(": ");
            if(headerParts.length == 2){
                headers.put(headerParts[0], headerParts[1]);
            }
        }
    }
    public static String getMethod(){
        return headers.get("Method");
    }

    public static String getHost(){
        return headers.get("Host");
    }

    public static String getPath(){
        return headers.get("Path");
    }
    public static String getAccept(){
        return headers.get("Accept");
    }

    public static String getUserAgent(){
        return headers.get("User-Agent");
    }

    public static String getCookie(){
        return headers.get("Cookie");
    }
}
