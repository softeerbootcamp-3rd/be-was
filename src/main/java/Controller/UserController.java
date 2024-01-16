package Controller;

import service.UserService;


public class UserController {

//    여기서 get, post, 경로에 따라서 유저 서비스의 어떤 메소드를 실행힐 건지 정하기
    public static void service(String method, String path, String params) {
        if (method.equals("GET")) {

            if (path.equals("create")) UserService.signUp(params);

        }
    }
}
