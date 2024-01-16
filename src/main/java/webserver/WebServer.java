package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    // 생성 가능한 스레드의 수를 스레드풀을 이용하여 제한.
    // 스레드의 개수가 한계치를 초과할 경우 나머지 스레드를 큐 형태로 대기 시킴.
    private static final ExecutorService executorService = Executors.newFixedThreadPool(200);
    //너무 오래 살아 있는 스레드를 KeepAlive함수를 이용하여 없앨 수 있으나 복잡해서 다음에 도전


    //멀티스레드를 사용하기 때문에 락이 걸리는 리스트를 선언
    static List<User> users =  Collections.synchronizedList(new ArrayList<>());

    public static void main(String args[]) throws Exception {

        //포트번호를 따로 지정했거나 안했거나의 경우의 수 나누기
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;

            // 새로운 클라이언트가 들어오기 전까지 Block 상태로 대기하는 함수는 listenSocket.accept()이다
            while ((connection = listenSocket.accept()) != null) {
                executorService.execute(new RequestHandler(connection));

                //아래는 기존의 코드(스레드 무한생성가능)
//                Thread thread = new Thread(new RequestHandler(connection));
//                thread.start();
            }
        }
        // 스레드풀 종료
        executorService.shutdown();
    }

}
