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
            ExecutorService executorService = Executors.newFixedThreadPool(16);
            try {
                // 클라이언트 연결을 계속해서 받아들이는 루프
                while (true) {
                    // 클라이언트의 연결을 받아들임
                    Socket connection = listenSocket.accept();

                    if (connection != null) {
                        // 스레드 풀에서 작업을 처리
                        executorService.execute(new dispatcherServlet(connection));
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
                logger.error("???");
            }
            finally
            {
                // 스레드 풀 종료
                executorService.shutdown();
            }
        }
    }


}
