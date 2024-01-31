package webserver;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

import config.*;
import controller.PageController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    //세션 관리를 위해 로컬 스레드를 선언
    public static final ThreadLocal<UUID> threadUuid = new ThreadLocal<>();
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            logger.debug("Ending Connected IP : {}, Port : {}", connection.getInetAddress(),
                    connection.getPort());

            HTTPRequest request = new HTTPRequest(br, logger);
            DataOutputStream dos = new DataOutputStream(out);




            //로그인 상태를 우선적으로 확인 후 로컬 스레드에 부여, 로그인이 안되어 있으면 null로 세팅한다
            loginCheck(request);

            //로그인 상태와 URL을 참고하여 접근하면 안되는 URL을 수정


            ControllerDispatcher controllerDispatcher = null;
            String url = request.getUrl();
            String urlFrontPart = url.split("\\?")[0];


            HTTPResponse response;
            // 페이지 불러오는 경우 (GET 메소드이며, url에 확장자가 있을 때)
            if(request.getMethod().equals("GET") && urlFrontPart.contains(".")){
                //로그인 상태에 따라 정적/동적 페이지 로드

                //로그인된 경우,html인 경우 동적페이지
                if(threadUuid.get() != null && url.contains(".html")) {
                    response = PageController.getPageDynamic(request);
                    System.out.println(url+1);
                }
                //로그인 안되어 있는 경우
                else if(Redirect.getNewUrl(url)!=null) {
                    response = PageController.RedirectStaticPage(Redirect.getNewUrl(url));
                    System.out.println(url+2);
                }

                //로그인이 되어 있는 경우
                else {
                    response = PageController.getPageStatic(request);
                    System.out.println(url+3);
                }
            }
            // 그 외에는 디스패쳐 서블릿으로 컨트롤러를 불러온다 (ControllerHandler가 디스패쳐 서블릿)
            else {
                for (ControllerDispatcher handler : ControllerDispatcher.values()) {
                    if (handler.url.equals(urlFrontPart)) {
                        controllerDispatcher = handler;
                        break;
                    }
                }
                // 적절한 컨트롤러 핸들러를 찾았을 경우, 해당 핸들러의 메소드 호출
                if (controllerDispatcher != null)
                    response = controllerDispatcher.toController(request);
                else
                    response = new HTTPResponse("HTTP/1.1", ResponseCode.NOT_FOUND.code, ResponseCode.NOT_FOUND.toString());
            }

            // 헤드와 바디 값을 DataOutputStream으로 전달
            byte[] head = response.getHead();
            byte[] body = response.getBody();
            dos.write(head, 0, head.length);
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void loginCheck(HTTPRequest request){
        String cookie = request.getHead().get("Cookie");
        UUID uuid = null;
        //쿠키에서 session id를 파싱한 후 Session에 존재하는지 확인
        try {
            if (cookie != null) {
                String[] cookieContents = cookie.split("; ");
                for (String content : cookieContents) {
                    if (content.startsWith("sid=")) {
                        UUID sid = UUID.fromString(content.substring("sid=".length()));
                        if (Session.getUserId(sid) != null) {
                            uuid = sid;
                            break;
                        }
                    }
                }
            }
        } catch(IllegalArgumentException ignored){
        } finally{
            threadUuid.set(uuid);
        }
    }

}
