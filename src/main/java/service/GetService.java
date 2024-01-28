package service;

import com.google.common.io.ByteStreams;
import db.Database;
import dto.request.HTTPRequestDto;
import dto.response.HTTPResponseDto;
import dto.session.Session;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collection;


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
        if(sessionId != null) {
            Session session = Database.findSessionById(sessionId);
            if(session != null)
                // 해당 세션 만료시키기
                session.invalidate();
        }
        // login.html로 리다이렉트
        return new HTTPResponseDto("/user/login.html");
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
        if(url.contains(".html"))
            path += "/templates" + url;
        // 2. css, fonts, images, js, ico일 경우
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

        // index.html일 경우 로그인 여부에 따라 동적인 화면 반환
        if(path.contains("index.html"))
            byteFile = makeIndex(httpRequestDto, byteFile);
        // list.html일 경우 전체 사용자 목록 동적 생성 후 반환
        if(path.contains("list.html"))
            byteFile = makeList(byteFile);
        if(path.contains("profile.html"))
            byteFile = makeProfile(httpRequestDto, byteFile);
        return byteFile;
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


    ///////////////////////////// 동적 화면 처리 ////////////////////////////////

    // index.html의 동적 화면 처리
    private byte[] makeIndex(HTTPRequestDto httpRequestDto, byte[] byteFile) {
        String sessionId = httpRequestDto.getSessionId();
        // 로그인 되어있지 않을 경우
        if(sessionId == null)
            return byteFile;
        // 로그인 되어있을 경우 -> 로그인 버튼의 '로그인'을 유저의 이름으로 변경
        User user = Database.findUserBySessionId(sessionId);
        if(user == null || user.getName() == null)
            return byteFile;
        return new String(byteFile).replace("로그인", user.getName()).getBytes();
    }

    // 전체 사용자 목록 추가하여 동적인 list.html 화면 반환
    private byte[] makeList(byte[] byteFile) {
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

    // 현재 로그인한 유저 정보로 profile.html 수정 후 반환
    private byte[] makeProfile(HTTPRequestDto httpRequestDto, byte[] byteFile) {
        String sessionId = httpRequestDto.getSessionId();
        User user = Database.findUserBySessionId(sessionId);
        if(user == null)
            return byteFile;

        String stringFile = new String(byteFile);
        stringFile = stringFile.replace("자바지기", user.getName());
        stringFile = stringFile.replace("javajigi@slipp.net", user.getEmail());
        return stringFile.getBytes();
    }
}
