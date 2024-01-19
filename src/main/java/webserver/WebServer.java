package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import logger.CustomLogger;

public class WebServer {

    private static final int DEFAULT_PORT = 8080;

    private final int port;
    private final ExecutorService executorService;

    public WebServer(int port) {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(30);
    }

    private void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            CustomLogger.printInfo("Web Application Server started " + port + " port.");

            Socket connection;
            while ((connection = serverSocket.accept()) != null) {
                executorService.execute(new HttpProcessor(connection));
            }
        } catch (Exception e) {
            CustomLogger.printError(e);
        } finally {
            executorService.shutdown();
        }
    }

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        WebServer webServer = new WebServer(port);
        webServer.start();
    }
}
