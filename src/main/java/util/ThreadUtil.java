package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {

    private static final int THREAD_POOL_SIZE = 100;
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    // concurrent package를 이용한 스레드 풀
    public static void startServer(int port) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                // 스레드 풀에서 스레드를 가져와서 처리
                executorService.execute(new RequestHandler(connection));
            }
        } catch (IOException e) {
            logger.error("Error while starting the server: {}", e.getMessage());
        } finally {
            // 스레드 풀 종료
            executorService.shutdown();
        }
    }
}
