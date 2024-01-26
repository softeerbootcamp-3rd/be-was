package controller;

import annotation.*;
import db.Database;
import model.User;
import util.SessionManager;
import util.StringParser;
import webserver.http.HttpRequest;
import webserver.http.HttpStatus;
import webserver.http.HttpHeader;
import webserver.http.ResponseEntity;

import java.util.*;


@Controller
@RequestMapping("/user")
public class UserController {

    @PostMapping(path = "/create")
    public static ResponseEntity signup(@RequestParam(name = "userId") String userId,
                                  @RequestParam(name = "password") String password,
                                  @RequestParam(name = "name") String name,
                                  @RequestParam(name = "email") String email) {

        User user = new User(userId, password, name, email);
        Database.addUser(user);

        Map<String, List<String>> headerMap = new HashMap<>();
        headerMap.put(HttpHeader.LOCATION, Collections.singletonList("/index.html"));

        return new ResponseEntity<>(
                HttpStatus.FOUND,
                headerMap
        );
    }

    @PostMapping(path = "/login")
    public static ResponseEntity login(@RequestParam(name = "userId") String userId,
                                 @RequestParam(name = "password") String password) {
        User user = Database.findUserById(userId);
        Map<String, List<String>> headerMap = new HashMap<>();

        if (!user.getPassword().equals(password)) {
            headerMap.put(HttpHeader.LOCATION, Collections.singletonList("/user/login_failed.html"));
            return new ResponseEntity<>(
                    HttpStatus.FOUND,
                    headerMap
            );
        }

        String SID = SessionManager.createSession();
        SessionManager.setAttribute(SID, "user", userId);
        List<String> cookies = new ArrayList<>();
        cookies.add("SID=" + SID);
        cookies.add("Max-Age=" + SessionManager.getSessionTimeoutSeconds());
        cookies.add("Path=" + "/");
        headerMap.put(HttpHeader.SET_COOKIE, cookies);
        headerMap.put(HttpHeader.LOCATION, Collections.singletonList("/index.html"));

        return new ResponseEntity<>(
                HttpStatus.FOUND,
                headerMap
        );
    }

    @GetMapping(path = "/logout")
    @ResponseBody
    public static ResponseEntity logout(HttpRequest httpRequest) {

        String SID = httpRequest.getCookie().split("=")[1];

        // 서버 세션 저장소에서 세션 삭제
        if (SessionManager.isSessinExist(SID))
            SessionManager.invalidateSession(SID);
        // 클라이언트에서도 쿠키 제거
        Map<String, List<String>> headerMap = new HashMap<>();
        List<String> cookies = new ArrayList<>();
        cookies.add("SID=" + SID);
        cookies.add("Max-Age=" + 0);
        cookies.add("Path=" + "/");
        headerMap.put(HttpHeader.SET_COOKIE, cookies);

        return new ResponseEntity<>(
                HttpStatus.OK,
                headerMap
        );
    }

}



