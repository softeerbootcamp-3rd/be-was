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
    private static final int THREAD_POOL_SIZE = 50; // 스레드 풀 사이즈 설정

    public static void main(String args[]) throws Exception {

        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            while (true) {
                Socket connection = listenSocket.accept();
                executorService.execute(new RequestHandler(connection));
            }
        } finally {
            executorService.shutdown();
        }
    }
}
