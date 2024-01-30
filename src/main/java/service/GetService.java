package service;

import com.google.common.io.ByteStreams;
import db.Database;
import dto.request.HTTPRequestDto;
import dto.response.HTTPResponseDto;
import model.Post;
import model.Session;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


public class GetService {

    ///////////////////////////// GET 요청 처리 ///////////////////////////////////

    private static final Logger logger = LoggerFactory.getLogger(GetService.class);

    // 회원가입 처리
    public HTTPResponseDto signup(HTTPRequestDto httpRequestDto) {

        if(httpRequestDto == null || httpRequestDto.getRequestParams() == null || httpRequestDto.getRequestParams().getMap().size() != 4)
            return new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());

        // User 객체 생성
        User user = new User(
                httpRequestDto.getRequestParams().getValue("userId"),
                httpRequestDto.getRequestParams().getValue("password"),
                httpRequestDto.getRequestParams().getValue("name"),
                httpRequestDto.getRequestParams().getValue("email"));

        // 필요한 정보가 제대로 들어오지 않았을 경우
        // 네가지 정보 모두 기입해야 회원가입 가능
        if(user.getUserId().equals("") || user.getPassword().equals("") || user.getName().equals("") || user.getEmail().equals(""))
            return new HTTPResponseDto(400, "text/plain", "모든 정보를 기입해주세요.".getBytes());

        // 중복 아이디 처리
        if(Database.findUserById(user.getUserId()) != null)
            return new HTTPResponseDto(200, "text/plain", "이미 존재하는 아이디입니다. 다시 시도해주세요.".getBytes());

        // 성공적인 회원가입 처리
        // 데이터베이스에 저장
        Database.addUser(user);
        logger.debug("새로운 유저: {}", user.toString());
        logger.debug("전체 DB: {}", Database.findAllUser());
        // /index.html로 리다이렉트
        return new HTTPResponseDto("/index.html");
    }

    // /logout 요청
    public HTTPResponseDto logout(HTTPRequestDto httpRequestDto) {
        String sessionId = httpRequestDto.getSessionId();
        HTTPResponseDto httpResponseDto = new HTTPResponseDto("/user/login.html");
        if(sessionId == null)
            return httpResponseDto;
        Session session = Database.findSessionById(sessionId);
        if(session == null)
            session = new Session("test");
        // 해당 세션 만료시키기
        session.invalidate();
        String setCookie = "sid=" + sessionId + "; expires=" + session.getExpiresWithFormat() + "; Path=/; secure; HttpOnly\r\n";
        httpResponseDto.setHeader("Set-Cookie", "Set-Cookie: " + setCookie);
        // 해당 세션 저장소에서 삭제
        Database.deleteSession(sessionId);
        return httpResponseDto;
    }

    // 파일 불러오기 요청
    public HTTPResponseDto requestFile(HTTPRequestDto httpRequestDto) {
        if(httpRequestDto.getRequestTarget() == null) {
            return new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());
        }

        // 파일 경로 찾기
        String path = findFilePath(httpRequestDto.getRequestTarget());
        logger.debug("file path: {}", path);

        // 파일 읽어오기
        byte[] byteFile = readFile(path, httpRequestDto);
        if(byteFile == null)
            return new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());
        return new HTTPResponseDto(200, httpRequestDto.getAccept(), byteFile);
    }

    // 파일 확장자에 따라 파일 경로 찾기
    private String findFilePath(String url) {
        String path = "./src/main/resources";
        // 1. html일 경우
        if(url.contains(".html")) {
            path += "/templates" + url;
            // .html 뒤에 추가 요청이 붙어있을 경우 제거
            if(path.contains(".html/"))
                path = path.substring(0, path.indexOf(".html")+".html".length());
        }
        // 2. css, fonts, images, js일 경우
        else if(url.contains("/css"))
            path += "/static" + url.substring(url.indexOf("/css"));
        else if(url.contains("/js"))
            path += "/static" + url.substring(url.indexOf("/js"));
        else if(url.contains("/fonts"))
            path += "/static" + url.substring(url.indexOf("/fonts"));
        else if(url.contains("/images"))
            path += "/static" + url.substring(url.indexOf("/images"));
        // 3. ico일 경우
        else
            path += "/static" + url;
        return path;
    }

    // 경로에 해당하는 파일 읽어오기
    private byte[] readFile(String path, HTTPRequestDto httpRequestDto) {
        byte[] byteFile = null;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(path);
            byteFile = ByteStreams.toByteArray(fis);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if(fis != null) { fis.close(); }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        if(byteFile == null)
            return null;

        // 동적 페이지 처리
        return makeDynamicPage(httpRequestDto, byteFile);
    }

    // 로그인 여부에 따른 파일 요청 처리
    public HTTPResponseDto showWithLogin(HTTPRequestDto httpRequestDto) {
        // 요청에 이미 세션이 있을 경우 기존 세션 아이디 가져오기
        String sessionId = httpRequestDto.getSessionId();
        // 로그인 상태가 아닐 경우
        if(sessionId == null)
            return new HTTPResponseDto("/user/login.html");
        // 로그인 상태일 경우
        return requestFile(httpRequestDto);
    }

    // 게시글 삭제 요청 처리
    public HTTPResponseDto deletePost(HTTPRequestDto httpRequestDto) {
        Long postId = 0L;
        // 게시글 아이디 가져오기
        try {
            String url = httpRequestDto.getRequestTarget();
            String postIdString = url.substring(url.indexOf("/qna/")+"/qna/".length(), url.lastIndexOf("/"));
            postId = Long.parseLong(postIdString);
        }
        catch (NumberFormatException e) {
            return new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());
        }
        // 작성자 여부 판단
        HTTPResponseDto httpResponseDto = isWriter(postId, httpRequestDto);
        if(httpResponseDto != null)
            return httpResponseDto;
        // 게시글 삭제
        Database.deletePost(postId);
        // index.html로 리다이렉트
        return new HTTPResponseDto("/");
    }

    // 게시글 수정 요청 처리
    public HTTPResponseDto updatePost(HTTPRequestDto httpRequestDto) {
        // 게시글 아이디 파싱
        try {
            String url = httpRequestDto.getRequestTarget();
            Long postId = Long.parseLong(url.substring(url.indexOf("form.html/")+"form.html/".length()));
            // 글 작성자인지 판단
            HTTPResponseDto httpResponseDto = isWriter(postId, httpRequestDto);
            if(httpResponseDto != null)
                return httpResponseDto;
            httpResponseDto = requestFile(httpRequestDto);
            // 기존 게시글 내용으로 띄워서 동적 페이지 반환
            if(httpResponseDto.getStatusCode() == 200)
                httpResponseDto.setBody(makeFormPost(httpResponseDto.getBody(), postId));
            return httpResponseDto;
        } catch (NumberFormatException e) {
            return new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());
        }
    }

    private HTTPResponseDto isWriter(Long postId, HTTPRequestDto httpRequestDto) {
        // 해당 게시글 가져오기
        Post post = Database.findPostById(postId);
        if(post == null)
            return new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());
        // 유저 정보 가져오기
        String sessionId = httpRequestDto.getSessionId();
        if(sessionId == null)
            return new HTTPResponseDto(200, "text/plain", "로그인 후 이용해주세요.".getBytes());
        User user = Database.findUserBySessionId(sessionId);
        if(user == null)
            return new HTTPResponseDto(400, "text/plain", "Bad Request".getBytes());
        // 작성자 여부 판단
        if(!post.getUserId().equals(user.getUserId()))
            return new HTTPResponseDto(200, "text/plain", "글 작성자가 아닙니다.".getBytes());
        return null;
    }


    ///////////////////////////// 동적 화면 처리 ////////////////////////////////

    private byte[] makeDynamicPage(HTTPRequestDto httpRequestDto, byte[] byteFile) {
        if(httpRequestDto.getRequestTarget().contains("index.html"))
            return makeIndex(httpRequestDto, byteFile);
        if(httpRequestDto.getRequestTarget().contains("list.html"))
            return makeUserList(byteFile);
        if(httpRequestDto.getRequestTarget().contains("profile.html"))
            return makeProfile(httpRequestDto, byteFile);
        if(httpRequestDto.getRequestTarget().contains("show.html"))
            return makeShowPost(httpRequestDto, byteFile);
        return byteFile;
    }

    // index.html의 동적 화면 처리
    private byte[] makeIndex(HTTPRequestDto httpRequestDto, byte[] byteFile) {
        String stringFile = new String(byteFile);
        // 1. 로그인 되어있을 경우 -> 로그인 버튼의 '로그인'을 유저의 이름으로 변경
        stringFile = replaceLogin2Name(stringFile, httpRequestDto.getSessionId());
        // 2. 게시물 목록 탐색하여 화면에 추가
        String front = stringFile.substring(0, stringFile.indexOf("<ul class=\"list\">") + "<ul class=\"list\">".length());
        String back = stringFile.substring(stringFile.indexOf("<div class=\"row\">"));
        String postList = getPostList() + "</ul>";
        return (front + postList + back).getBytes();
    }

    // 로그인 버튼의 '로그인'을 유저의 이름으로 변경
    private String replaceLogin2Name(String stringFile, String sessionId) {
        if(sessionId == null)
            return stringFile;
        User user = Database.findUserBySessionId(sessionId);
        if(user == null || user.getName() == null)
            return stringFile;
        return stringFile.replace("로그인", user.getName());
    }

    // 게시물 저장소에 저장되어있는 전체 게시물 목록 html 형식으로 반환
    private String getPostList() {
        StringBuilder sb = new StringBuilder();
        List<Post> posts = Database.findAllPost();
        Collections.sort(posts);

        for(int i = 0; i < posts.size(); i++) {
            String row = makePostListOneRow(posts.get(i), i+1);
            sb.append(row);
        }

        return sb.toString();
    }

    // 한 개의 게시물에 대해 index.html에 띄울 행 문자열 반환
    private String makePostListOneRow(Post post, int index) {
        if(post == null)
            return "";

        return "<li><div class=\"wrap\"><div class=\"main\"><strong class=\"subject\"><a href=\"./qna/show.html/"
                + post.getId() + "\">"
                + post.getTitle() + "</a></strong><div class=\"auth-info\"><i class=\"icon-add-comment\"></i><span class=\"time\">"
                + post.getCreateAtWithFormat() + " </span><a href=\"./user/profile.html/"
                + post.getUserId() + "\" class=\"author\">"
                + post.getUserId() + "</a></div><div class=\"reply\" title=\"댓글\"><i class=\"icon-reply\"></i><span class=\"point\">"
                + index + "</span></div></div></div></li>";
    }

    // 전체 사용자 목록 추가하여 동적인 list.html 화면 반환
    private byte[] makeUserList(byte[] byteFile) {
        String stringFile = new String(byteFile);
        // 사용자 목록을 추가하기 전 앞부분과 뒷부분의 html 파싱
        String front = stringFile.substring(0, stringFile.indexOf("</tbody>"));
        String back = stringFile.substring(stringFile.indexOf("</tbody>"));
        String userList = getUserList();
        return (front + userList + back).getBytes();
    }

    // 세션 저장소에 저장되어있는 전체 사용자 목록 html 형식으로 반환
    private String getUserList() {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for(Session session : Database.findAllSession()) {
            User user = Database.findUserById(session.getUserId());
            String row = makeUserListOneRow(++index, user);
            sb.append(row);
        }
        return sb.toString();
    }

    // 한명의 사용자에 대해 list.html에 띄울 행 문자열 반환
    private String makeUserListOneRow(int index, User user) {
        if(user == null)
            return "";
        return "<tr><th scope=\"row\">" + index
                + "</th><td>" + user.getUserId()
                + "</td><td>" + user.getName()
                + "</td> <td>" + user.getEmail()
                + "</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td></tr>";
    }

    // 동적 profile.html 반환
    private byte[] makeProfile(HTTPRequestDto httpRequestDto, byte[] byteFile) {

        String url = httpRequestDto.getRequestTarget();
        User user = null;
        if(url.equals("/user/profile.html") || url.equals("/user/profile.html/")) {     // 현재 로그인한 유저로 반환
            String sessionId = httpRequestDto.getSessionId();
            user = Database.findUserBySessionId(sessionId);
        }
        else {      // 요청으로 들어온 유저로 반환
            String userId = url.substring(url.indexOf("profile.html/")+"profile.html/".length());
            user = Database.findUserById(userId);
        }
        if(user == null)
            return null;

        String stringFile = new String(byteFile);
        stringFile = stringFile.replace("자바지기", user.getName());
        stringFile = stringFile.replace("javajigi@slipp.net", user.getEmail());
        return stringFile.getBytes();
    }

    // 게시물 하나에 대해 세부사항 동적 페이지 반환
    private byte[] makeShowPost(HTTPRequestDto httpRequestDto, byte[] byteFile) {
        try {
            String url = httpRequestDto.getRequestTarget();
            Long postId = Long.parseLong(url.substring(url.indexOf("show.html/")+"show.html/".length()));
            // 해당하는 게시물 가져오기
            Post post = Database.findPostById(postId);
            if(post == null)
                return null;
            return replaceShowPost(byteFile, post);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private byte[] replaceShowPost(byte[] byteFile, Post post) {
        String stringFile = new String(byteFile);
        stringFile = stringFile.replace("제목", post.getTitle());
        stringFile = stringFile.replace("글쓴이", post.getWriterName());
        stringFile = stringFile.replace("작성 시간", post.getCreateAtWithFormat());
        // 내용 주입
        StringBuilder sb = new StringBuilder();
        String[] tokens = post.getContents().split("\n");
        for(int i = 0; i < tokens.length; i++) {
            sb.append(tokens[i]);
            if(i == tokens.length - 1)
                break;
            sb.append("</p><p>");
        }
        stringFile = stringFile.replace("내용", sb.toString());
        // 수정, 삭제 링크 걸기
        stringFile = stringFile.replace("/qna/form.html/12345", "/qna/form.html/" + post.getId());
        stringFile = stringFile.replace("/qna/12345/delete", "/qna/" + post.getId() + "/delete");
        return stringFile.getBytes();
    }

    // 기존 게시글 내용 띄워서 수정 폼 반환
    private byte[] makeFormPost(byte[] body, Long postId) {
        // 기존 게시글 가져오기
        Post post = Database.findPostById(postId);
        String stringFile = new String(body);
        // 글쓴이 넣기
        stringFile = stringFile.replace(
                "<input class=\"form-control\" id=\"writer\" name=\"writer\" placeholder=\"글쓴이\"/>",
                "<input class=\"form-control\" id=\"writer\" name=\"writer\" placeholder=\"글쓴이\""
                        + " value=\"" + post.getWriterName() + "\"/>");
        // 제목 넣기
        stringFile = stringFile.replace(
                "<input type=\"text\" class=\"form-control\" id=\"title\" name=\"title\" placeholder=\"제목\"/>",
                "<input type=\"text\" class=\"form-control\" id=\"title\" name=\"title\" placeholder=\"제목\""
                        + " value=\"" + post.getTitle() + "\"/>"
        );
        // 내용 넣기
        stringFile = stringFile.replace(
                "<textarea name=\"contents\" id=\"contents\" rows=\"5\" class=\"form-control\"></textarea>",
                "<textarea name=\"contents\" id=\"contents\" rows=\"5\" class=\"form-control\">"
                        + post.getContents() + "</textarea>"
        );
        // 질문하기 버튼 -> 수정하기 버튼
        stringFile = stringFile.replace("질문하기", "수정하기");
        return stringFile.getBytes();
    }

}
