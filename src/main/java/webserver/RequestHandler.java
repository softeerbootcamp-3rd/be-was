package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ControllerMapper;
import util.RequestBuilder;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("ip", connection.getInetAddress().toString());
            requestMap.put("port", String.valueOf(connection.getPort()));

            String[] array = line.split(" ");
            requestMap.put("httpMethod", array[0]);
            requestMap.put("uri", array[1]);
            requestMap.put("httpVersion", array[2]);

            while (!(line = br.readLine()).isEmpty()) {
                String[] components = line.split(":", 2);
                if (components[1].startsWith(" "))
                    components[1] = components[1].substring(1);
                requestMap.put(components[0], components[1]);
            }

            RequestBuilder requestBuilder = new RequestBuilder(
                    requestMap.get("ip"),
                    requestMap.get("port"),
                    requestMap.get("httpMethod"),
                    requestMap.get("uri"),
                    requestMap.get("httpVersion"),
                    requestMap.get("Host"),
                    requestMap.get("Connection"),
                    requestMap.get("User-Agent"),
                    requestMap.get("Referer"),
                    requestMap.get("Accept"),
                    requestMap.get("Accept-Language"),
                    requestMap.get("Accept-Encoding")
            );

            logger.debug(requestBuilder.toString());

            BiConsumer<OutputStream, String> controller = ControllerMapper.getController(requestBuilder.getHttpMethod());
            if (controller != null) {
                controller.accept(out, requestBuilder.getUri());
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}