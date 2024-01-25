package webserver;

import java.io.*;
import java.net.Socket;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

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

            if(splitHeader[1].contains("html")) {
                logger.debug("splitHeader: " + splitHeader[1]);
                reqPath += splitHeader[1];
            }

            // 회원가입 정보 추출
            if(splitHeader[1].contains("create")) {
                String userId = null, userPwd = null, userName = null, userEmail = null;

                String[] userInfo = splitHeader[1].split("&");
                String[] idSplit = userInfo[0].split("=");
                userId = idSplit[1];

                String[] pwdSplit = userInfo[1].split("=");
                userPwd = pwdSplit[1];

                String[] nameSplit = userInfo[2].split("=");
                userName = nameSplit[1];

                String[] emailSplit = userInfo[3].split("=");
                userEmail = emailSplit[1];

                // 유저 저장
                User user = new User(userId, userPwd, userName, userEmail);
                Database.addUser(user);

                // 저장된 유저정보 확인
                logger.debug("UserId : " + user.getUserId());
                logger.debug("UserPwd : " + user.getPassword());
                logger.debug("UserName : " + user.getName());
                logger.debug("UserEmail : " + user.getEmail());
            }

            //logger.debug(reqPath);
            DataOutputStream dos = new DataOutputStream(out);
            String readResult = readFile(reqPath);

            byte[] body = readResult.getBytes();

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
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

    public String readFile(String filePath) {
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            br = new BufferedReader(new FileReader(filePath));
            String ln = null;

            while((ln = br.readLine()) != null) {
                sb.append(ln);
            }
        } catch (IOException e) {
            logger.debug(e.getMessage());
        } finally {
            try {
                if(br != null) br.close();
            } catch (Exception e) {}
        }
        return sb.toString();
    }
}

