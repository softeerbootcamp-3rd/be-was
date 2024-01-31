package controller;

import config.HTTPResponse;
import config.ResponseCode;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

class PageControllerTest {
    @Test
    void runHtmlParse(){
        String url = "http://localhost:8080/index.html";
        File file;
        String TEMPLATE_FILE_PATH = "/Users/user/IdeaProjects/be-was/src/main/resources/templates";
        String STATIC_FILE_PATH = "/Users/user/IdeaProjects/be-was/src/main/resources/static";
        if (url.contains(".html"))
            file = new File(TEMPLATE_FILE_PATH + url);
        else
            file = new File(STATIC_FILE_PATH + url);

        HTTPResponse response = null;

        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));

            String line;
            StringBuilder sb = new StringBuilder();
            while((line = bf.readLine()) != null){
                //<li><a href="../user/login.html" role="button">로그인</a></li>
                //<li><a href="../user/form.html" role="button">회원가입</a></li>

                //<li><a href="user/form.html" role="button">회원가입</a></li>
                //<li><a href="user/login.html" role="button">로그인</a></li>
                if(line.contains("role=\"button\">회원가입</a></li>"))
                    continue;
                else if(line.contains("role=\"button\">로그인</a></li>")){
                    sb.append(line.replace("role=\"button\">","class=\"disabled\" role=\"button\">").replace("로그인","안녕하세용"));
                    sb.append(System.lineSeparator());
                    continue;
                }

                sb.append(line);
                sb.append(System.lineSeparator());
            }

        }
        catch (Exception e){

        }

    }

}