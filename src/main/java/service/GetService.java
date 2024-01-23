package service;

import db.Database;
import dto.HTTPRequestDto;
import dto.HTTPResponseDto;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


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
        logger.debug("전체 DB: {}", Database.findAll());
        // /index.html로 리다이렉트
        return showIndex();
    }

    // 파일 불러오기 요청
    public HTTPResponseDto requestFile(HTTPRequestDto httpRequestDto) throws IOException {
        if(httpRequestDto.getRequestTarget() == null) {
            return HTTPResponseDto.createResponseDto(400, "text/plain", "Bad Request".getBytes());
        }
        // 해당 파일을 읽고 응답
        String path = "./src/main/resources";

        // 1. html일 경우
        if(httpRequestDto.getRequestTarget().contains(".html")) {
            path += "/templates" + httpRequestDto.getRequestTarget();
        }
        // 2. css, fonts, images, js, ico일 경우
        else {
            path += "/static" + httpRequestDto.getRequestTarget();
        }
        logger.debug("file path: {}", path);
        return HTTPResponseDto.createResponseDto(200,
                httpRequestDto.getAccept(),
                Files.readAllBytes(new File(path).toPath()));
    }

    // index.html로 리다이렉트
    public HTTPResponseDto showIndex() {
        HTTPResponseDto httpResponseDto = HTTPResponseDto.createResponseDto(302, null, null);
        httpResponseDto.addHeader("Location", "/index.html");
        return httpResponseDto;
    }

}
