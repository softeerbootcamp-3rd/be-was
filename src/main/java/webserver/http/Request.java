package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Request {
    String httpMethod;
    String requestTarget;
    String httpVersion;
    Float httpVersionNum;
    private final ArrayList<String> headerContent;
    private final ArrayList<String> bodyContent;
    HashMap<String, String> requestHeader;
    HashMap<String, String> requestBody;

    public Request(BufferedReader br) throws IOException {
        headerContent = new ArrayList<>();
        bodyContent = new ArrayList<>();
        parseRequest(br);
        requestHeader = new HashMap<>();
        requestBody = new HashMap<>();

        parseRequestStartLine(headerContent.get(0));
        parseRequestHeader();
        parseRequestBody();
    }

    private void parseRequestHeader() {
        for(int i = 1;i < headerContent.size();i++) {
            if(headerContent.get(i).isEmpty())
                break;

            String[] keyValue = headerContent.get(i).split(" ");
            String key = keyValue[0].substring(0, keyValue[0].length() - 1);
            String val = keyValue[1];
            requestHeader.put(key, val);
        }
    }
    private void parseRequestBody() {
        for(int i = 0;i < bodyContent.size();i++) {
            if(bodyContent.get(i).isEmpty())
                break;

            String[] keyValue = bodyContent.get(i).split(" ");
            String key = keyValue[0].substring(0, keyValue[0].length() - 1);
            String val = keyValue[1];
            requestBody.put(key, val);
        }
    }


    private void parseRequestStartLine(String startLine) {
        String[] requestStartLine = startLine.split(" ");
        this.httpMethod = requestStartLine[0];
        this.requestTarget = requestStartLine[1];
        this.httpVersion = requestStartLine[2];
        httpVersionNum = Float.parseFloat(httpVersion.split("/")[1]);
    }

    private void parseRequest(BufferedReader br) throws IOException {
        String line;
        boolean hasBody = false;
        int blankCount = 0;
        while ((line = br.readLine()) != null) {
            if (line.contains("Content-Length")) {
                hasBody = true;
            }
            if (!hasBody && line.isEmpty()) {
                break;
            }
            if (blankCount == 1 && line.isEmpty()) {
                break;
            }
            if (line.isEmpty()) {
                blankCount++;
            }

            if (blankCount < 1) {
                headerContent.add(line);
            } else {
                bodyContent.add(line);
            }
        }
    }

    public void print()
    {
        System.out.println("*******************************************");
        System.out.println("httpMethod : " + this.httpMethod);
        System.out.println("requestTarget : " + this.requestTarget);
        System.out.println("httpVersion : " + this.httpVersion);
        System.out.println("httpVersionNum : " + this.httpVersionNum);

        System.out.println("requestHeader : ");
        for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("requestBody : ");
        for (Map.Entry<String, String> entry : requestBody.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("*******************************************");
    }

}
