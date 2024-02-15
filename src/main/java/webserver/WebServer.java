package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import db.Database;
import model.Qna;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    // 생성 가능한 스레드의 수를 스레드풀을 이용하여 제한.
    // 스레드의 개수가 한계치를 초과할 경우 나머지 스레드를 큐 형태로 대기 시킴.

    private static final int MAX_THREADS = 32;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);




    public static void main(String args[]) throws Exception {

        //포트번호를 따로 지정했거나 안했거나의 경우의 수 나누기
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }
        //더미데이터 생성

        Database.addQna(new Qna("코딩초보", "스레드랑 프로세스 차이점", "스레드랑 프로세스가 뭐가 달라요? \n잘 모르곘어요"));
        Database.addQna(new Qna("eyeben", "이거 왜 안돼요?", "String str = url.split(\".\")[0];"));
        Database.addQna(new Qna("코딩초보", "마지막 질문", "asdfjsadfkjs"));

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;

            // 새로운 클라이언트가 들어오기 전까지 Block 상태로 대기하는 함수는 listenSocket.accept()이다
            while ((connection = listenSocket.accept()) != null) {
                executorService.execute(new RequestHandler(connection));

            }
        }
        // 스레드풀 종료
        executorService.shutdown();
    }

}
