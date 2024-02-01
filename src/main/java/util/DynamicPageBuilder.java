package util;

import config.HTTPRequest;
import config.HTTPResponse;
import config.Session;
import controller.PageController;
import db.Database;
import model.Qna;
import model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static webserver.RequestHandler.threadUuid;

public class DynamicPageBuilder {
    public static HashMap<String, PageBuilder> dynamicPageBuilders = new HashMap<>();
    private static String STATIC_FILE_PATH = "/Users/user/IdeaProjects/be-was/src/main/resources/static";
    private static String TEMPLATE_FILE_PATH = "/Users/user/IdeaProjects/be-was/src/main/resources/templates";
    static{

        dynamicPageBuilders.put("/index",DynamicPageBuilder::buildIndex);
        dynamicPageBuilders.put("/qna/show",DynamicPageBuilder::buildQnaShow);
        dynamicPageBuilders.put("/qna/form",DynamicPageBuilder::buildQnaForm);
        dynamicPageBuilders.put("/user/list",DynamicPageBuilder::buildUserList);
    }
    
    private static byte[] buildUserList(HTTPRequest request) throws Exception{
        //유저를 세션으로 구함
        byte[] body;
        String userId = Session.getUserId(threadUuid.get());
        User user = Database.findUserById(userId);
        
        //파일 읽어오기
        String url = request.getUrl();
        File file = new File( TEMPLATE_FILE_PATH + url);
        BufferedReader bf = new BufferedReader(new FileReader(file));

        String line;
        StringBuilder sb = new StringBuilder();

        StringBuilder txt = new StringBuilder();
        //유저 불러오기
        for(User u : Database.findAll()) {
            txt.append("<tr>\n<td>");
            txt.append(u.getUserId());
            txt.append("</td> <td>");
            txt.append(u.getName());
            txt.append("</td> <td>");
            txt.append(u.getEmail());
            txt.append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n</tr>");
        }
        // 바디에 추가
        while ((line = bf.readLine()) != null) {
            if (line.contains("추가"))
                sb.append(line.replace("추가", txt));

            else if(line.contains("로그인"))
                sb.append(line.replace("로그인", user.getName()));
            else
                sb.append(line);
            sb.append(System.lineSeparator());
        }
        body = sb.toString().getBytes();
        return body;

    }

    private static byte[] buildQnaForm(HTTPRequest request) {
        HTTPResponse response = PageController.getPageStatic(request);
        return response.getBody();
    }

    private static byte[] buildQnaShow(HTTPRequest request) throws Exception {
        //세션으로 유저 찾음
        byte[] body = null;
        String userId = Session.getUserId(threadUuid.get());
        User user = Database.findUserById(userId);
        // 파일 읽어온 후 재구성
        String url = request.getUrl();
        String[] urlSplit = url.split("/");
        Long qnaId = Long.parseLong(urlSplit[urlSplit.length-1]);
        File file = new File( TEMPLATE_FILE_PATH + url.replace("/"+urlSplit[urlSplit.length-1], ""));

        BufferedReader bf = new BufferedReader(new FileReader(file));
        Qna qna = Database.findQnaById(qnaId);
        String line;
        StringBuilder sb = new StringBuilder();

        
        while ((line = bf.readLine()) != null) {
            if (line.contains("role=\"button\">회원가입</a></li>")) {
                sb.append("<li><a href=\"#\" role=\"button\">로그아웃</a></li>\n" +
                        "                <li><a href=\"#\" role=\"button\">개인정보수정</a></li>");
                continue;
            }
            else if (line.contains("role=\"button\">로그인</a></li>")) {
                sb.append(line.replace("role=\"button\">", "class=\"disabled\" role=\"button\">").replace("로그인", user.getName()));
                sb.append(System.lineSeparator());
                continue;
            }
            if (line.contains("제목"))
                sb.append(line.replace("제목", qna.getTitle()));

            else if (line.contains("작성자"))
                sb.append(line.replace("작성자", qna.getWriter()));

            else if(line.contains("작성시간")) {
                String parsedLocalDateTimeNow = qna.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                sb.append(line.replace("작성시간", parsedLocalDateTimeNow));
            }

            else if(line.contains("내용"))
                sb.append(line.replace("내용", qna.getContent()));

            else
                sb.append(line);
            sb.append(System.lineSeparator());
        }
        body = sb.toString().getBytes();
        return body;
    }


    private static byte[] buildIndex(HTTPRequest request) throws Exception {
        //세션으로 유저 찾기
        byte[] body = null;
        String userId = Session.getUserId(threadUuid.get());
        User user = Database.findUserById(userId);
        //파일 읽어 온 후 재구성
        String url = request.getUrl();
        File file = new File( TEMPLATE_FILE_PATH + url);
        BufferedReader bf = new BufferedReader(new FileReader(file));

        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = bf.readLine()) != null) {
            // html 일부분 수정
            if (line.contains("role=\"button\">회원가입</a></li>")) {
                sb.append("<li><a href=\"#\" role=\"button\">로그아웃</a></li>\n" +
                        "                <li><a href=\"#\" role=\"button\">개인정보수정</a></li>");
                continue;
            }
            else if (line.contains("role=\"button\">로그인</a></li>")) {
                sb.append(line.replace("role=\"button\">", "class=\"disabled\" role=\"button\">").replace("로그인", user.getName()));
                sb.append(System.lineSeparator());
                continue;
            }

            sb.append(line);
            sb.append(System.lineSeparator());
            // \n 을 사용하면 안되는 이유:
            // 운영체제마다 줄바꿈 형식이 다름, System.lineSeparator를 사용하면
            // 운영체제에 맞게 JVM가 설정해줌
        }

        body = sb.toString().getBytes();
        return body;
    }
}
