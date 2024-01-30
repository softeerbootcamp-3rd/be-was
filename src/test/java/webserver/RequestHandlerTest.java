package webserver;


import org.junit.jupiter.api.Test;

import static config.ResponseCode.OK;


class RequestHandlerTest {

    @Test
    void run() {

        ControllerDispatcher controllerDispatcher = null;
        for (ControllerDispatcher handler : ControllerDispatcher.values()) {
            if (handler.url.equals("/index.html")) {
                System.out.println("good");
                controllerDispatcher = handler;
                break;
            }
        }
    }

    @Test
    void run2() {
        System.out.println(OK.code);
        System.out.println(OK);
    }

    @Test
    void run3() {
        byte[] body = new byte[0];
        System.out.println(body.length);
    }

}