package webserver;

import util.URIParer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private Map<String, String> header;
    private Map<String, String> params;
    private Map<String, String> body;

    public HttpRequest(Socket socket, InputStream in) throws IndexOutOfBoundsException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("ip", socket.getInetAddress().toString());
        headerMap.put("port", String.valueOf(socket.getPort()));

        String[] array = line.split(" ");
        String httpMethod = array[0];
        headerMap.put("httpMethod", httpMethod);
        headerMap.put("path", array[1]);
        headerMap.put("httpVersion", array[2]);

        while (!(line = br.readLine()).isEmpty()) {
            String[] components = line.split(":", 2);
            if (components[1].startsWith(" "))
                components[1] = components[1].substring(1);

            headerMap.put(components[0], components[1]);
        }

        if (httpMethod.equals("GET")) {
            String[] uri = array[1].split("\\?");
            Map<String, String> paramsMap = new HashMap<>();
            if (uri.length > 1) {
                paramsMap = URIParer.parserKeyValue(uri[1]);
            }
            this.params = paramsMap;
        } else if (httpMethod.equals("POST")) {
            Map<String, String> bodyMap = new HashMap<>();
            Integer bodySize = Integer.parseInt(headerMap.get("Content-Length"));

            char[] bodyArr = new char[bodySize];
            String bodyStr = "";
            if ((br.read(bodyArr, 0, bodySize)) != -1) {
                bodyStr = new String(bodyArr, 0, bodySize);
            }
            bodyMap = URIParer.parserKeyValue(bodyStr);
            this.body = bodyMap;
        }
        this.header = headerMap;
    }

    public String getHttpMethod() {
        return header.get("httpMethod");
    }

    public String getPath() {
        return header.get("path");
    }

    public Map<String, String> getParams() {
        if (params != null)
            return params;
        if (body != null)
            return body;
        return null;
    }

    @Override
    public String toString() {
        return "Request [ip=" + header.get("ip") + ", port=" + header.get("port")
                + ", method=" + header.get("httpMethod") + ", path=" + header.get("path")
                + ", http_version=" + header.get("httpVersion") + ", host=" + header.get("Host")
                + ", accept=" + header.get("Accept") + "]";
    }


}
