package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import db.Database;
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

            // 세션 저장소 가비지 컬렉터 실행
            Timer scheduler = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Database.sessionGC();
                    System.out.println("가비지 컬렉터 실행! 사이즈: " + Database.findAllSession().size());
                }
            };
            scheduler.scheduleAtFixedRate(task, 60*60*1000, 60*60*1000);        // 1시간 후 1시간 간격으로 세션 저장소 청소

            ExecutorService executorService = Executors.newFixedThreadPool(10);     // 고정 크키 5의 쓰레드 풀 생성

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                // 쓰레드를 생성하고 실행
                executorService.submit(new RequestHandler(connection));
            }
            executorService.shutdown();     // 작업 종료
            scheduler.cancel();             // 세션 저장소 가비지 컬렉터 종료
        }
    }
}
