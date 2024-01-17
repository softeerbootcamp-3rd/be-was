package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {

    private String ip;
    private String port;
    private String httpMethod;
    private String uri;
    private String httpVersion;
    private String host;
    private String connection;
    private String userAgent;
    private String referer;
    private String accept;
    private String acceptLanguage;
    private String acceptEncoding;

    public RequestBuilder(Socket socket, InputStream in) {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            Map<String, String> requestMap = new HashMap<>();
            this.ip = socket.getInetAddress().toString();
            this.port = String.valueOf(socket.getPort());

            String[] array = line.split(" ");
            this.httpMethod = array[0];
            this.uri = array[1];
            this.httpVersion = array[2];

            while (!(line = br.readLine()).isEmpty()) {
                String[] components = line.split(":", 2);
                if (components[1].startsWith(" "))
                    components[1] = components[1].substring(1);

                switch (components[0]) {
                    case "Host":
                        this.host = components[1];
                        break;
                    case "Connection":
                        this.connection = components[1];
                        break;
                    case "User-Agent":
                        this.userAgent = components[1];
                        break;
                    case "Referer":
                        this.referer = components[1];
                        break;
                    case "Accept":
                        this.accept = components[1];
                        break;
                    case "Accept-Language":
                        this.acceptLanguage = components[1];
                        break;
                    case "Accept-Encoding":
                        this.acceptEncoding = components[1];
                        break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getUri() {
        return uri;
    }


    @Override
    public String toString() {
        return "Request [ip=" + ip + ", port=" + port + ", method=" + httpMethod + ", uri=" + uri +
                ", http_version=" + httpVersion + ", host=" + host + ", accept=" + accept + "]";
    }


}
