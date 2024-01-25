package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String args[]) throws Exception {
        int port;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }
        // ExecutorService를 이용한 스레드 풀 생성
        ExecutorService threadPool =  new ThreadPoolExecutor(5, 20, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                // 스레드 풀을 이용하여 작업 스케줄링
                threadPool.submit(new RequestHandler(connection));
            }
        }
    }
}


