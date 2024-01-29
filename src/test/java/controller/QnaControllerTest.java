package controller;

import model.Qna;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Test
    @DisplayName("Qna별로 썸네일 html이 잘 생성되는지 테스트")
    public void run2(){
        Qna qna = new Qna("이름", "제목", "내용");
        StringBuilder sb = new StringBuilder("");
        sb.append("<li>" +
                "<div class=\"wrap\">" +
                "<div class=\"main\">" +
                "<strong class=\"subject\">" +
                "<a href=\"./qna/show.html\">");

        sb.append(qna.getTitle());
        sb.append("</a>" +
                "</strong>" +
                "<div class=\"auth-info\">" +
                "<i class=\"icon-add-comment\"></i>" +
                "<span class=\"time\">");

        sb.append(qna.getCreationTime());
        sb.append("</span>" +
                "<a href=\"./user/profile.html\" class=\"author\">");

        sb.append(qna.getWriter());
        sb.append("</a>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</li>");
        System.out.println(sb.toString());
    }
}