package service;

import com.google.common.io.ByteStreams;
import db.Database;
import dto.request.HTTPRequestDto;
import dto.response.HTTPResponseDto;
import dto.session.Session;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;


public class GetService {

    ///////////////////////////// GET 요청 처리 ///////////////////////////////////

    private static final Logger logger = LoggerFactory.getLogger(GetService.class);

    // 회원가입 처리
    public HTTPResponseDto signup(HTTPRequestDto httpRequestDto) {

        if(httpRequestDto == null || httpRequestDto.getRequestParams() == null || httpRequestDto.getRequestParams().size() != 4)
            return HTTPResponseDto.createResponseDto(400, "text/plain", "Bad Request".getBytes());

        // User 객체 생성
        User user = new User(
                httpRequestDto.getRequestParams().get("userId"),
                httpRequestDto.getRequestParams().get("password"),
                httpRequestDto.getRequestParams().get("name"),
                httpRequestDto.getRequestParams().get("email"));

        // 필요한 정보가 제대로 들어오지 않았을 경우
        // 네가지 정보 모두 기입해야 회원가입 가능
        if(user.getUserId().equals("") || user.getPassword().equals("") || user.getName().equals("") || user.getEmail().equals(""))
            return HTTPResponseDto.createResponseDto(400, "text/plain", "모든 정보를 기입해주세요.".getBytes());

        // 중복 아이디 처리
        if(Database.findUserById(user.getUserId()) != null) {
            return HTTPResponseDto.createResponseDto(200, "text/plain", "이미 존재하는 아이디입니다. 다시 시도해주세요.".getBytes());
        }

        // 성공적인 회원가입 처리
        // 데이터베이스에 저장
        Database.addUser(user);
        logger.debug("새로운 유저: {}", user.toString());
        logger.debug("전체 DB: {}", Database.findAllUser());
        // /index.html로 리다이렉트
        return HTTPResponseDto.create302Dto("/index.html");
    }

    // 파일 불러오기 요청
    public HTTPResponseDto requestFile(HTTPRequestDto httpRequestDto) throws IOException {
        if(httpRequestDto.getRequestTarget() == null) {
            return HTTPResponseDto.createResponseDto(400, "text/plain", "Bad Request".getBytes());
        }

        // 파일 경로 찾기
        String path = findFilePath(httpRequestDto.getRequestTarget());
        logger.debug("file path: {}", path);

        return HTTPResponseDto.createResponseDto(200,
                httpRequestDto.getAccept(),
                readFile(path, httpRequestDto));
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
            e.printStackTrace();
        } finally {
            try {
                if(fis != null) { fis.close(); }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // index.html일 경우 로그인 여부에 따라 동적인 화면 반환
        if(path.contains("index.html"))
            byteFile = makeIndexWithLoginInfo(httpRequestDto, byteFile);
        return byteFile;
    }

    // index.html의 동적 화면 처리
    private byte[] makeIndexWithLoginInfo(HTTPRequestDto httpRequestDto, byte[] byteFile) {
        String sessionId = httpRequestDto.getSessionId();
        // 로그인 되어있지 않을 경우
        if(sessionId == null)
            return byteFile;
        // 로그인 되어있을 경우 -> 로그인 버튼의 '로그인'을 유저의 이름으로 변경
        // 유저의 이름 가져오기
        String name = findUserName(sessionId);
        if(name == null)
            return byteFile;
        return indexReplaceWithName(byteFile, name);
    }

    // 세션 아이디를 이용하여 해당하는 유저의 이름 반환
    private String findUserName(String sessionId) {
        Session session = Database.findSessionById(sessionId);
        if(session == null || session.getUserId() == null)
            return null;
        User user = Database.findUserById(session.getUserId());
        if(user == null)
            return null;
        return user.getName();
    }

    // index.html 파일에서 '로그인'을 유저의 이름으로 대체하여 반환
    private byte[] indexReplaceWithName(byte[] byteFile, String name) {
        String indexHtml = new String(byteFile);
        indexHtml = indexHtml.replace("로그인", name);
        return indexHtml.getBytes();
    }
}
