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
    public static void main(String[] args) throws Exception{
        int port = initPort(args);
        // 쓰레드 풀 생성
        ExecutorService executor = Executors.newFixedThreadPool(10);
        // 서버소켓을 생성
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);
            // 클라이언트가 연결될때까지 대기
            listen(listenSocket, executor);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }finally {
            executor.shutdown();
        }
    }

    private static void listen(ServerSocket listenSocket, ExecutorService executor) throws IOException {
        Socket connection;
        while ((connection = listenSocket.accept()) != null) {
            Runnable worker = new RequestController(connection);
            executor.execute(worker);
        }
    }

    // port 초기화
    private static int initPort(String[] args) {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }
        return port;
    }
}
