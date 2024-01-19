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
    private static final int MAX_THREAD = 10;

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // Concurrent 패키지 사용
        // newSingleThreadExecutor : 한번에 하나의 작업만 수행, 스레드 1개로 구성된 스레드 풀 생성
        // newFixedThreadPool : n개의 작업을 동시에 실행, 스레드 n개로 구성된 스레드 풀 셍성
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREAD);

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                executor.execute(new RequestHandler(connection));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            // pool shutdown
            executor.shutdown();
        }
    }
}
