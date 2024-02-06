package webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 스레드풀을 생성한다. 개수는 사용가능한 코어갯수에 비례하고,
        // 정적 파일을 읽는 IO-Bound 작업이 많은 점에서 32개로 설정했다.
        ExecutorService threadPool = Executors.newFixedThreadPool(32);

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                threadPool.submit(new RequestHandler(connection));
            }
        } catch (IOException e) {
            logger.debug("connection: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }
}
