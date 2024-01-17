package controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.RequestUrl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    @Test
    void create() throws IOException {
        //Given
        String firstLine = "GET /user/create?userId=gyeongsu&password=1234&name=gyeongsu&email=gyeongsu@hyundai.com HTTP/1.1";
        RequestUrl url = RequestUrl.of(firstLine);
        UserController userController = new UserController(url);

        //When
        byte[] body = userController.run();

        //Then
        assertArrayEquals("gyeongsu created".getBytes(), body);
    }
}
