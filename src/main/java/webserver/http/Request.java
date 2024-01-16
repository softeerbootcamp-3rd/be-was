package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Request {
    String httpMethod;
    String requestTarget;
    String httpVersion;
    Float httpVersionNum;
    private ArrayList<String> headerContent;
    private ArrayList<String> bodyContent;
    HashMap<String, String> requestHeader;
    HashMap<String, String> requestBody;

    public Request(BufferedReader br) throws IOException {
        headerContent = new ArrayList<>();
        bodyContent = new ArrayList<>();
        parseRequest(br);
        requestHeader = new HashMap<>();
        requestBody = new HashMap<>();

        ParseRequestStartLine(headerContent.get(0));
    }

    private void ParseRequestStartLine(String startLine) {
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

    public void Print()
    {
        System.out.println("httpMethod : " + this.httpMethod);
        System.out.println("requestTarget : " + this.requestTarget);
        System.out.println("httpVersion : " + this.httpVersion);
        System.out.println("httpVersionNum : " + String.valueOf(this.httpVersionNum));

        for(String it : headerContent)
            System.out.println(it);
        for(String it : bodyContent)
            System.out.println(it);
    }

}
