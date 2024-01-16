package service;

import db.Database;
import dto.HTTPRequestDto;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class Service {


    // 회원가입 처리
    public static byte[] signup(String[] requestParams) {
        if(requestParams == null || requestParams.length != 4)
            return "다시 시도해주세요.".getBytes();
        // User 객체 생성
        User user = new User(requestParams[0], requestParams[1], requestParams[2], requestParams[3]);
        // 데이터베이스에 저장
        Database.addUser(user);
        System.out.println(user.toString());
        return ("Hello, " + user.getName() + "!").getBytes();
    }

    // 파일 불러오기 요청
    public static byte[] requestFile(HTTPRequestDto httpRequestDto) throws IOException {
        // 해당 파일을 읽고 응답
        String path = "./src/main/resources";
        // 1. html일 경우
        if(httpRequestDto.getRequest_target().contains(".html")) {
            path += "/templates" + httpRequestDto.getRequest_target();
        }
        // 2. css, fonts, images, js, ico일 경우
        else {
            path += "/static" + httpRequestDto.getRequest_target();
        }
        return Files.readAllBytes(new File(path).toPath());
    }
}
