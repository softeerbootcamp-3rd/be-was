package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // ExecutorService를 생성하여 스레드 풀을 관리
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                // 기존 Thread를 사용하던 코드
//                Thread thread = new Thread(new RequestHandler(connection));
//                thread.start();


                // 요청을 처리하는 작업을 Runnable로 생성
                Runnable requestHandler = new RequestHandler(connection);
                // ExecutorService를 이용하여 작업을 실행
                executorService.execute(requestHandler);
            }
        }
    }
}
