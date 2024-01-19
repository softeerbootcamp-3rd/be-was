package webserver;

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

    public HttpRequest(Socket socket, InputStream in) throws IndexOutOfBoundsException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("ip", socket.getInetAddress().toString());
        headerMap.put("port", String.valueOf(socket.getPort()));

        String[] array = line.split(" ");
        headerMap.put("httpMethod", array[0]);
        String[] uri = array[1].split("\\?");
        headerMap.put("path", uri[0]);
        headerMap.put("httpVersion", array[2]);

        Map<String, String> paramsMap = new HashMap<>();
        if (uri.length > 1) {
            for (String param : uri[1].split("&")) {
                paramsMap.put(param.split("=")[0], param.split("=")[1]);
            }
        }

        while (!(line = br.readLine()).isEmpty()) {
            String[] components = line.split(":", 2);
            if (components[1].startsWith(" "))
                components[1] = components[1].substring(1);

            headerMap.put(components[0], components[1]);
        }

        this.header = headerMap;
        this.params = paramsMap;
    }

    public String getHttpMethod() {
        return header.get("httpMethod");
    }

    public String getPath() {
        return header.get("path");
    }

    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "Request [ip=" + header.get("ip") + ", port=" + header.get("port")
                + ", method=" + header.get("httpMethod") + ", path=" + header.get("path")
                + ", http_version=" + header.get("httpVersion") + ", host=" + header.get("Host")
                + ", accept=" + header.get("Accept") + "]";
    }


}
