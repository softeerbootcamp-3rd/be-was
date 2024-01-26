package webserver.http;

import util.StringParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private String ip;
    private String port;
    private String method;
    private String path;
    private String protocol;
    private HttpHeader requestHeader;
    private Map<String, String> params;
    private Map<String, String> body;

    public HttpRequest(Socket socket, InputStream in) throws IndexOutOfBoundsException, IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        // Read the request line
        String requestLine = br.readLine();
        String[] requestLineParts = requestLine.split(" ");
        this.ip = socket.getInetAddress().toString();
        this.port = String.valueOf(socket.getPort());
        this.method = requestLineParts[0];
        this.path = requestLineParts[1];
        this.protocol = requestLineParts[2];

        Map<String, List<String>> headers = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(":");
            String headerName = headerParts[0];
            String headerValue = headerParts[1];
            if (headerName.startsWith(" "))
                headerName = headerName.substring(1);
            headers.computeIfAbsent(headerName, key -> {
                return List.of(headerValue);
            });
        }
        this.requestHeader = new HttpHeader(headers);

        this.params = StringParser.parseQueryString(requestLineParts[1]);

        if (requestHeader.getContentLength() != null) {
            Integer bodySize = Integer.parseInt(requestHeader.getContentLength());
            char[] bodyArr = new char[bodySize];
            String bodyStr = "";
            if ((br.read(bodyArr, 0, bodySize)) != -1) {
                bodyStr = new String(bodyArr, 0, bodySize);
            }
            Map<String, String> bodyMap = StringParser.parseKeyVaue(bodyStr);
            this.body = bodyMap;
        }
    }

    public String getHttpMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        if (params != null)
            return params;
        if (body != null)
            return body;
        return null;
    }

    public String getCookie() {
        return requestHeader.getCookie();
    }

    @Override
    public String toString() {
        return "Request [ip=" + ip + ", port=" + port
                + ", method=" + method + ", path=" + path
                + ", http_version=" + protocol + ", host=" + requestHeader.getHost()
                + ", accept=" + requestHeader.getAccept() + ", cookie=" + getCookie() + "]";
    }


}
