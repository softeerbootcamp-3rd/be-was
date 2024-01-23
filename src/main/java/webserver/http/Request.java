package webserver.http;

import utils.RequestBodyParse.RequestBodyParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static webserver.http.Mime.convertMime;

public class Request {
    private String httpMethod;
    private String requestTarget;
    private String httpVersion;
    private Float httpVersionNum;
    private Mime responseMimeType;
    private final ArrayList<String> headerContent = new ArrayList<>();
    private char[] bodyContent;
    private final HashMap<String, String> requestHeader = new HashMap<>();
    private HashMap<String, String> requestBody;

    public Request(BufferedReader br) throws IOException {
        parseRequest(br);
        parseRequestStartLine(headerContent.get(0));
        parseRequestHeader();
        parseRequestBody();
        RequestHandler requestHandler = new RequestHandler();
        requestHandler.handleRequest(this);
    }

    public void addRequestHeader(String key, String val){
        requestHeader.put(key, val);
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
        if(bodyContent.length == 0)
        {
            requestBody = new HashMap<>();
            return;
        }

        RequestBodyParser requestBodyParser = new RequestBodyParser(bodyContent);
        requestBody = (HashMap<String, String>) requestBodyParser.contentTypeBodyParse(ContentType.convertContentType(requestHeader.get("Content-Type")));
    }


    private void parseRequestStartLine(String startLine) {
        String[] requestStartLine = startLine.split(" ");
        this.httpMethod = requestStartLine[0];
        this.requestTarget = requestStartLine[1];
        this.httpVersion = requestStartLine[2];
        int lastDotIndex = requestTarget.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex < requestTarget.length() - 1) {
            this.responseMimeType = convertMime(requestTarget.substring(lastDotIndex + 1));
        } else {
            this.responseMimeType = Mime.NONE;
        }
        httpVersionNum = Float.parseFloat(httpVersion.split("/")[1]);
    }

    private void parseRequest(BufferedReader br) throws IOException {
        String line;
        int contentLength = 0;
        while (!(line = br.readLine()).isEmpty()) {
            headerContent.add(line);
            if(line.contains("Content-Length")){
                contentLength = Integer.parseInt(line.split(" ")[1]);
            }
        }

        bodyContent = new char[contentLength];
        br.read(bodyContent, 0, contentLength);
    }

    public void print()
    {
        System.out.println("*******************************************");
        System.out.println("httpMethod : " + this.httpMethod);
        System.out.println("requestTarget : " + this.requestTarget);
        System.out.println("httpVersion : " + this.httpVersion);
        System.out.println("httpVersionNum : " + this.httpVersionNum);
        System.out.println("mime : " + this.responseMimeType.getMimeType());
        System.out.println("[ requestHeader ]");
        for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println("[ requestBody ]");
        if(!requestBody.isEmpty()){
            for (Map.Entry<String, String> entry : requestBody.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
        System.out.println("*******************************************");
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public Mime getResponseMimeType() {
        return responseMimeType;
    }

    public HashMap<String, String> getRequestHeader() {
        return requestHeader;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
