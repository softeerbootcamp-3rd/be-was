package webserver;

import model.ResponseEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ResponseEnumTest {

    @DisplayName("확장자로 path를 찾는다.")
    @Test
    void getPathName(){
        //given
        String extension = "ico";

        //when
        String pathName = ResponseEnum.getPathName(extension);

        //then
        assertThat(pathName).isEqualTo("src/main/resources/static");
    }

    @DisplayName("확장자로 response의 contentType을 찾는다.")
    @Test
    void getContentType(){
        //given
        String extension = "ico";

        //when
        String contentType = ResponseEnum.getContentType(extension);

        //then
        assertThat(contentType).isEqualTo("image/x-icon");
    }

}
