
import Controller.User.UserController;
import HTTPModel.HttpStatus;
import HTTPModel.Request;
import HTTPModel.Response;
import HandlerMapping.HandlerMapping;
import db.Database;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static webserver.HTTPHandler.RequestMake.MakeRequest;


public class ControllerTest {


    @BeforeEach
    void init(){
        User user  = new User("1","1234","a","a@naver.com");
        Database.addUser(user);
    }


    @Test
    public void indexTest() throws Exception{

        String apiUrl = "http://localhost:8080/index.html";
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

    }


    @Test
    public void signupEnterTest() throws Exception{

        String apiUrl = "http://localhost:8080/user/form.html";
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

    }


    InputStream requestString(String userId, String password, String name, String email){
        String testString = "POST /user/create HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n" +
                "Content-Length: " + 55 + "\n\n" +
                "userId=" + userId + "&password=" + password + "&name=" + name + "&email=" + email;

        return new ByteArrayInputStream(testString.getBytes());
    }

    @Test
    @DisplayName("회원가입 성공하는 controller 시나리오")
    public void signup() throws Exception{

        Request request = new Request();
        String userId=  "8";
        String email = "a@naver.com";
        request = MakeRequest(requestString(userId,"1234","a",email));

        HandlerMapping handlerMapping = new HandlerMapping(request);

        Response response = handlerMapping.Controller();

        assertEquals(response.getHttpStatus(), HttpStatus.REDIRECT);
        assertEquals(Database.findUserById(userId).getEmail(), email);

    }


    @Test
    @DisplayName("회원가입 실패하는 controller 시나리오")
    public void signupFail() throws Exception{

        Request request = new Request();
        String userId=  "1";
        String email = "a@naver.com";
        request = MakeRequest(requestString(userId,"1234","a",email));

        HandlerMapping handlerMapping = new HandlerMapping(request);

        Response response = handlerMapping.Controller();

        assertEquals(response.getHttpStatus(), HttpStatus.OK);
        assertEquals(Database.findAll().size(), 1);

    }


}
