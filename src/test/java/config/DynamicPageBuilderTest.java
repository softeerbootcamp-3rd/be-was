package config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DynamicPageBuilderTest {

    @Test
    @DisplayName("URL에서 qna Id 파싱되는지")
    public void run1(){

        String url = "qna/form.html/13";
        String[] urlSplit = url.split("/");
        Long qnaId = Long.parseLong(urlSplit[urlSplit.length-1]);

        assertEquals(13L, qnaId);
        String url2 =  url.replace("/"+urlSplit[urlSplit.length-1], "");
        assertEquals("qna/form.html",url2);
    }

}