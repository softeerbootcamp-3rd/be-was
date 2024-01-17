package utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ResponseTest {

    Response response = new Response();

    @Test
    public void contentType() {
        //given
        String case1 = "/css/bootstrap.min.css";
        String case2 = "/js/scripts.js";
        String case3 = "/favicon.ico";
//        String case4 = "/fonts/glyphicons-halflings-regular.ttf";
//        String case5 = "/images/80-text.png";
        String case6 = "/index.html";

        //when
        String result1 = response.getContentType(case1);
        String result2 = response.getContentType(case2);
        String result3 = response.getContentType(case3);
//        String result4 = response.getContentType(case4);
//        String result5 = response.getContentType(case5);
        String result6 = response.getContentType(case6);

        //then
        assertThat(result1).isEqualTo("text/css");
        assertThat(result2).isEqualTo("text/javascript");
        assertThat(result3).isEqualTo("image/x-icon");
//        assertThat(result4).isEqualTo("");
//        assertThat(result5).isEqualTo("");
        assertThat(result6).isEqualTo("text/html");
    }
}