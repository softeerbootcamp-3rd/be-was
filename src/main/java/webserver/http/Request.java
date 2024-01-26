package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RequestBodyParse.RequestBodyParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static webserver.http.HttpMethod.convertHttpMethodType;
import static webserver.http.Mime.convertMime;

public class Request {
    private static final Logger logger = LoggerFactory.getLogger(Request.class);

    private HttpMethod httpMethod;
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
        requestBody = (HashMap<String, String>) RequestBodyParser.contentTypeBodyParse(ContentType.convertContentType(requestHeader.get("Content-Type")), bodyContent);
    }
    private void parseRequestStartLine(String startLine) {
        String[] requestStartLine = startLine.split(" ");
        this.httpMethod = convertHttpMethodType(requestStartLine[0]);
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
        logger.debug("*******************************************");
        logger.debug("httpMethod : {}", this.httpMethod);
        logger.debug("requestTarget : {}", this.requestTarget);
        logger.debug("httpVersion : {}", this.httpVersion);
        logger.debug("httpVersionNum : {}", this.httpVersionNum);
        logger.debug("mime : {}", this.responseMimeType.getMimeType());
        logger.debug("[ requestHeader ]");
        for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
            logger.debug("{} : {}", entry.getKey() ,entry.getValue());
        }
        logger.debug("[ requestBody ]");
        if(!requestBody.isEmpty()){
            for (Map.Entry<String, String> entry : requestBody.entrySet()) {
                logger.debug("{} {}",entry.getKey(), entry.getValue());
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

    public Map<String, String> getRequestHeader() {
        return requestHeader;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Map<String, String> getRequestBody() {
        return requestBody;
    }
}
