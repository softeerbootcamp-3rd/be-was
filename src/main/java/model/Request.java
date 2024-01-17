package model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import utils.Response;

public class Request {

    private static final Response response = new Response();

    private final String method;
    private final String file;
    private final String http;
    private final int port;
    private final String contentType;
    private final String filePath;

    public Request(InputStream in, int portNumber, Logger logger) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(inputStreamReader);
        String header = br.readLine();

        String[] parse = header.split(" ");
        method = parse[0];
        file = parse[1];
        http = parse[2];
        port = portNumber;
        contentType = response.getContentType(file);
        filePath = response.getPath(file, contentType);

        logger.debug("port {} || method : {}, http : {}, file : {}", port, method, http, file);

        // 나머지 헤더 로깅
//        String line = br.readLine();
//        while (!line.isEmpty()) {
//            line = br.readLine();
//            logger.debug(line);
//        }
    }

    public String getMethod() {
        return method;
    }

    public int getPort() {
        return port;
    }

    public String getFile() {
        return file;
    }

    public String getHttp() {
        return http;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFilePath() {
        return filePath;
    }
}
