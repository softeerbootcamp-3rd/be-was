package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();

            if(line == null) {
                return;
            }
            logger.debug("# Request Line");
            logger.debug(line);
            logger.debug("# Request Header");

            String[] tokens = line.split(" ");
            final Set<String> printedKey = new HashSet<>(Arrays.asList("Connection",
                                                                        "Host",
                                                                        "User-Agent",
                                                                        "Cookie"));
            while(true) {
                line = br.readLine();
                if(line.equals("")) break;
                String[] keyAndValue = line.split(": ");
                String key = keyAndValue[0], value = keyAndValue[1];
                if(printedKey.contains(key)) logger.debug(line);
            }

            String url = tokens[1];
            if(url.startsWith("/user/create")) {
                int index = url.indexOf("?");
                String queryString = url.substring(index + 1);
                HashMap<String, String> params = queryStringParsing(queryString);
                String userId = params.get("userId");
                String password = params.get("password");
                String name = params.get("name");
                String email = params.get("email");
                User user = new User(userId, password, name, email);

                String result = verifyUser(user);
                if(verifyUser(user).equals("성공")) {
                    Database.addUser(user);
                    logger.debug("새로운 유저 생성! " + user.toString() + "\n");
                }
                else {
                    logger.debug(result + "\n");
                }
            } else {
                byte[] body = Files.readAllBytes(
                        new File("./src/main/resources/templates" + tokens[1]).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
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

    public static HashMap<String, String> queryStringParsing(String queryString) {
        HashMap<String, String> queries = new HashMap<>();

        String[] keyAndValue = queryString.split("&");
        for(String keyValue : keyAndValue) {
            int indexOfEqual = keyValue.indexOf("=");
            String key = keyValue.substring(0, indexOfEqual);
            String value = keyValue.substring(indexOfEqual+1);
            queries.put(key, value);
        }
        return queries;
    }

    public static String verifyUser(User user) {
        if(user.getUserId().isEmpty()
        || user.getEmail().isEmpty()
        || user.getName().isEmpty()
        || user.getPassword().isEmpty()) return "입력란에 공백이 존재하면 안됩니다.";
        else if(Database.findUserById(user.getUserId()) != null) return "중복되는 아이디 입니다.";
        return "성공";
    }
}
