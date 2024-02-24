package webserver;

import com.google.common.base.Strings;
import constant.HttpStatus;
import exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.web.SessionManager;
import util.web.RequestMapper;
import util.web.SecureChecker;
import model.SharedData;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Objects;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpResponse response;
            try {
                HttpRequest request = new HttpRequest(in);
                SharedData.request.set(request);
                SharedData.requestUser.set(SessionManager.getLoggedInUser(request));

                logger.debug("Connection IP : {}, Port : {}, request: {}",
                        connection.getInetAddress(), connection.getPort(), request.getPath());

                String redirectPath = SecureChecker.checkRedirect(request);
                if (!Strings.isNullOrEmpty(redirectPath))
                    response = HttpResponse.redirect(redirectPath);
                else
                    response = serveResource(request);
            } catch (ResourceNotFoundException e) {
                e.printStackTrace();
                response = HttpResponse.of(HttpStatus.NOT_FOUND);
            } catch (IllegalArgumentException | IndexOutOfBoundsException | NoSuchMethodException
                     | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                response = HttpResponse.of(HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("error processing request: {}", e.getMessage());
                response = HttpResponse.of(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            response.send(out, logger);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpResponse serveResource(HttpRequest request)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException,
            IOException {

        Method handler = RequestMapper.getMethod(request);
        if (handler != null) {
            return RequestMapper.invoke(handler);
        } else if (Objects.equals(request.getMethod(), "GET")) {
            return HttpResponse.forward(request.getPath());
        } else
            throw new ResourceNotFoundException(request.getPath());
    }
}
