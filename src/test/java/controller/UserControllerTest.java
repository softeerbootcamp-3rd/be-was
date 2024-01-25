package controller;

import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import http.status.HttpStatus;
import model.User;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest {

    @Test
    void testSignup() throws IOException {
        UserController userController = UserController.getInstance();
        HttpRequest request = HttpRequest.createFromReader(new BufferedReader(
                new StringReader("POST /user/create HTTP/1.1\r\n" +
                        "Content-Length: 58\r\n" +
                        "\r\n" +
                        "userId=asdf&password=asdf&name=asdf&email=asdf%40gmail.com")
                ));
        HttpResponse response = HttpResponse.of();

        userController.signup(request, response);

        assertThat(response.getHeader().getStatus())
                .isEqualTo(HttpStatus.FOUND);

        assertThat(response.getHeader().getProperty("Location"))
                .isEqualTo("/user/login.html");

        User user = Database.findUserById("asdf");
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("asdf@gmail.com");
    }
}