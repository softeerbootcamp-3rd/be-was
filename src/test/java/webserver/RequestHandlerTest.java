package webserver;


import org.junit.jupiter.api.Test;

import static config.ResponseCode.OK;


class RequestHandlerTest {

    @Test
    void run() {

        ControllerHandler controllerHandler = null;
        for (ControllerHandler handler : ControllerHandler.values()) {
            if (handler.url.equals("/index.html")) {
                System.out.println("good");
                controllerHandler = handler;
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