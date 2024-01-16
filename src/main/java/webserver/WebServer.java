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
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String args[]) throws Exception {

        //포트 설정 코드
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // ExecutorService를 사용하여 스레드 풀 생성
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {  //socket에 클라이언트와 접속 이어줌(accept())->클라이언트 들어오면 소켓 반환
                executorService.execute(new RequestHandler(connection));
            }
        }
        finally {
            //프로그램 종료 시 스레드 풀 종료
            executorService.shutdown();
        }
    }
}
