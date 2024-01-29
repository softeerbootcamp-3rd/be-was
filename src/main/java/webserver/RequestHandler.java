package webserver;

import java.io.*;
import java.net.Socket;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection; //클라이언트-서버 간 연결
    public String reqType = null;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            logger.debug("request line ; {}", line);
            logger.debug("header : " + line);

            String[] splitHeader = line.split(" ");
            String reqPath = "src/main/resources/templates";
            int bodyLength = 0;
            String userInfoString = null;
            DataOutputStream dos = new DataOutputStream(out);

            // POST request 읽어오기
            char[] bodyContent = null;
            int c;
            if(splitHeader[0].equals("POST")) {
                // Content-Length 확인
                while((line = br.readLine()) != null) {
                    logger.debug(line);
                    if(line.contains("Content-Length")){
                        bodyLength = Integer.parseInt(line.split(" ")[1]);

                    }
                    if(line.isEmpty()) {
                        logger.debug("<<<<Http body part>>>>");
                        char[] httpBody = new char[bodyLength];
                        br.read(httpBody);
                        userInfoString = new String(httpBody);
                        logger.debug("userInfoString : " + userInfoString);
                        break;
                    }
                }
            }

            // extract path
            int idx = splitHeader[1].lastIndexOf(".");
            String ext = splitHeader[1].substring(idx+1);

            if(ext.equals("html")) {
                logger.debug("splitHeader: " + splitHeader[1]);
                reqPath += splitHeader[1];
                reqType = "text/html";
            }
            if(ext.equals("css")) {
                logger.debug("splitHeader: " + splitHeader[1]);
                reqPath = "src/main/resources/static" + splitHeader[1];
                reqType = "text/css";
            }
            if(ext.equals("woff")) {
                logger.debug("splitHeader: " + splitHeader[1]);
                reqPath = "src/main/resources/static" + splitHeader[1];
                reqType = "font/woff";
            }
            if(ext.equals("ttf")) {
                logger.debug("splitHeader: " + splitHeader[1]);
                reqPath = "src/main/resources/static" + splitHeader[1];
                reqType = "font/ttf";
            }
            if(ext.equals("js")) {
                logger.debug("splitHeader: " + splitHeader[1]);
                reqPath = "src/main/resources/static" + splitHeader[1];
                reqType = "application/javascript";
            }
            if(ext.equals("ico")) {
                logger.debug("splitHeader: " + splitHeader[1]);
                reqPath = "src/main/resources/static" + splitHeader[1];
                reqType = "image/x-icon";
            }

            if(ext.equals("png")) {
                logger.debug("splitHeader: " + splitHeader[1]);
                reqPath = "src/main/resources/static" + splitHeader[1];
                reqType = "image/png";
            }
            if(ext.equals("jpg")) {
                logger.debug("splitHeader: " + splitHeader[1]);
                reqPath = "src/main/resources/static" + splitHeader[1];
                reqType = "image/jpg";
            }

            // 로그인 시도
            if(splitHeader[1].contains("login") && splitHeader[0].equals("POST")) {
                String userId = null, userPwd = null, userName = null, userEmail = null;

                if(splitHeader[0].equals("POST")) { // 삭제 예정
                    String[] userInfo = userInfoSplit(userInfoString,false);
                    userId = userInfo[0];
                    userPwd = userInfo[1];
                    logger.debug(">>>>>>>userId : " + userId + " | " + "userPwd : " + userPwd);
                }

                User userData;

                if( (userData = Database.findUserById(userId)) != null) {
                    // 로그인 성공
                    if(userData.getPassword().equals(userPwd)) {
                        logger.debug(">>>>>>>>login success");
                        response302Header(dos, "index.html");
                    }
                    // 로그인 실패(비밀번호 불일치)
                    else {
                        logger.debug(">>>>>>>>login failed - wrong password");
                        response302Header(dos, "user/login_failed.html");
                    }
                }
                //로그인 실패(미등록 유저)
                else {
                    logger.debug(">>>>>>>>login failed - unregistered user");
                    response302Header(dos, "user/login_failed.html");
                }

            }
            // 회원가입 정보 추출 & 리다이렉트
            else if(splitHeader[1].contains("create")) {
                String userId = null, userPwd = null, userName = null, userEmail = null;
                String[] userInfo = null;
                if(splitHeader[0].equals("GET")) {
                    userInfo = splitHeader[1].split("&");
                    String[] idSplit = userInfo[0].split("=");
                    userId = idSplit[1];

                    String[] pwdSplit = userInfo[1].split("=");
                    userPwd = pwdSplit[1];

                    String[] nameSplit = userInfo[2].split("=");
                    userName = nameSplit[1];

                    String[] emailSplit = userInfo[3].split("=");
                    userEmail = emailSplit[1];
                }
                else if(splitHeader[0].equals("POST")) {
                    userInfo = userInfoString.split("&");
                    String[] idSplit = userInfo[0].split("=");
                    userId = idSplit[1];

                    String[] pwdSplit = userInfo[1].split("=");
                    userPwd = pwdSplit[1];

                    String[] nameSplit = userInfo[2].split("=");
                    userName = nameSplit[1];

                    String[] emailSplit = userInfo[3].split("=");
                    userEmail = emailSplit[1];
                }


                // 유저 저장
                User user = new User(userId, userPwd, userName, userEmail);
                Database.addUser(user);

                // 저장된 유저정보 확인
                logger.debug("UserId : " + user.getUserId());
                logger.debug("UserPwd : " + user.getPassword());
                logger.debug("UserName : " + user.getName());
                logger.debug("UserEmail : " + user.getEmail());

                response302Header(dos, "index.html");

            }

            byte[] body = readFile(reqPath);

            response200Header(dos, body.length, reqType);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            if(contentType.equals("html"))
                dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            else
                dos.writeBytes("Content-Type: " + reqType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location) {
        logger.debug(">>>>>>Response 302");
        try{
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            // 리다이렉트 위치
            String redirectPath = "Location : http://localhost:8080/" + location;
            dos.writeBytes(redirectPath);
            System.out.println(redirectPath);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String[] userInfoSplit(String userInfoString, boolean extraInfo) {
        String userId, userPwd, userName = null, userEmail = null;

        String[] userInfo = userInfoString.split("&");

        userId = userInfo[0].split("=")[1];
        userPwd = userInfo[1].split("=")[1];
        if(extraInfo) {
            userName = userInfo[2].split("=")[1];
            userEmail = userInfo[3].split("=")[1];
        }

        String[] userInfoSplitted = {userId, userPwd, userName, userEmail};
        return userInfoSplitted;
    }

    public byte[] readFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);

        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] content = new byte[(int) file.length()];
            fileInputStream.read(content);
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


