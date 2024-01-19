
import Controller.User.UserController;
import DTO.Response;
import HandlerMapping.HandlerMapping;
import db.Database;
import model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import webserver.RequestHandler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ControllerTest {


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


    @Test
    public void signup() throws Exception{



        String apiUrl = "http://localhost:8080/user/create?userId=24&password=1234&name=a&email=a%40naver.com";
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();


        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check HTTP status code
            assertEquals(200, response.statusCode());

            // Ensure UserController logic is executed
            UserController userController = new UserController("create?userId=24&password=1234&name=a&email=a%40naver.com");
            byte[] body = userController.UserLogic();

            // Print or log the state of the Database
            Collection<User> userList = Database.findAll();
            System.out.println("User List after UserController logic: " + userList);

            // Assertions based on the UserController logic
            assertEquals(1, userList.size()); // Check if the user is added

        } catch (Exception e) {
            e.printStackTrace();
        }


        /*

        HandlerMapping handlerMapping = new HandlerMapping();
        handlerMapping.urlParsing("/user/create?userId=13&password=1234&name=a&email=a%40naver.com",new Response());
        Collection<User> userList = Database.findAll();
        System.out.println(userList);

        assertEquals(1,Database.findAll().size());

        */

    }


    @Test
    public void dbtest(){

        Database.addUser(new User("1","1234","a","a@naver.com"));
        assertEquals("a", Database.findUserById("1").getName());

    }

}
