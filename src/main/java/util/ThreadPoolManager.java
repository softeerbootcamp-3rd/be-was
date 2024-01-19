package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.DispatcherServlet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadPoolManager {

    private static final int THREAD_POOL_SIZE = 100;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    public static void startServer(int port) { // 스레드 풀을 생성하고 스레드를 실행
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                executorService.execute(new DispatcherServlet(connection)); // 스레드 풀에서 스레드를 가져와서 실행
            }
        } catch (IOException e) {
            logger.error("Error while starting the server: {}", e.getMessage());
        } finally {
            executorService.shutdown(); // 스레드 풀 종료
        }
    }
}
