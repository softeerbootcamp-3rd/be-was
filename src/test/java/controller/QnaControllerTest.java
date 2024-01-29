package controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QnaControllerTest {

    @Test
    @DisplayName("QNA 파싱이 제대로 되는지 테스트")
    public void run1(){
        String txt = "writer=writerr&title=titlee&contents=contentt";
        List<String> bodyStrings = new ArrayList<>(Arrays.asList(txt.split("&")));
        String title = null;
        String writer = null;
        String content = null;

        for(String str : bodyStrings){
            if(str.contains("writer"))
                writer = str.substring("writer=".length());
            else if(str.contains("title"))
                title = str.substring("title=".length());
            else if(str.contains("content"))
                content = str.substring("contents=".length());
        }

        assertEquals("writerr", writer);
        assertEquals("titlee", title);
        assertEquals("contentt", content);
    }
}