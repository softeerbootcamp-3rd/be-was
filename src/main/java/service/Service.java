package service;

import controller.Controller;
import db.Database;
import dto.HTTPRequestDto;
import dto.HTTPResponseDto;
import model.User;
import webserver.RequestHandler;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;


public class Service {


    // 회원가입 처리
    public static HTTPResponseDto signup(HTTPRequestDto httpRequestDto) throws IOException {

        if(httpRequestDto == null || httpRequestDto.getRequestParams() == null || httpRequestDto.getRequestParams().size() != 4)
            return new HTTPResponseDto(404, "다시 시도해주세요.".getBytes());

        // User 객체 생성
        User user = new User(
                httpRequestDto.getRequestParams().get("userId"),
                httpRequestDto.getRequestParams().get("password"),
                httpRequestDto.getRequestParams().get("name"),
                httpRequestDto.getRequestParams().get("email"));

        // 필요한 정보가 제대로 들어오지 않았을 경우
        // 네가지 정보 모두 기입해야 회원가입 가능
        if(!(!user.getUserId().equals("") && !user.getPassword().equals("") && !user.getName().equals("") && !user.getEmail().equals("")))
            return new HTTPResponseDto(200, "모든 정보를 기입해주세요.".getBytes());

        // 중복 아이디 처리
        if(Database.findUserById(user.getUserId()) != null)
            return new HTTPResponseDto(200, "이미 존재하는 아이디입니다. 다시 시도해주세요.".getBytes());

        // 성공적인 회원가입 처리
        // 데이터베이스에 저장
        Database.addUser(user);
        System.out.println("새로운 유저: " + user.toString());
        System.out.println("전체 DB: " + Database.findAll());
        // /index.html로 리다이렉트
//        HTTPRequestDto newHttpRequestDto = new HTTPRequestDto();
//        newHttpRequestDto.setHTTPMethod("GET");
//        newHttpRequestDto.setRequestTarget("/index.html");
//        newHttpRequestDto.setHTTPVersion(httpRequestDto.getHTTPVersion());
//        newHttpRequestDto.setHost(httpRequestDto.getHost());
//        newHttpRequestDto.setAccept("text/html");
//        return Controller.doRequest(newHttpRequestDto);
        return new HTTPResponseDto(200, ("Hello, " + user.getName() + "!").getBytes());
    }

    // 파일 불러오기 요청
    public static HTTPResponseDto requestFile(HTTPRequestDto httpRequestDto) throws IOException {
        if(httpRequestDto.getRequestTarget() == null)
            return new HTTPResponseDto(404, "다시 시도해주세요.".getBytes());
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
        return new HTTPResponseDto(200, Files.readAllBytes(new File(path).toPath()));
    }
}
