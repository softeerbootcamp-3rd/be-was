package dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserSignUpDtoTest {

    @DisplayName("body의 문자열을 UserSignUpDto로 변환할 수 있다.")
    @Test
    void userDtoFrom(){
        //given
        String query = "userId=user1&password=1234&name=test&email=test@naver.com";

        //when
        UserSignUpDto userSignUpDto = UserSignUpDto.from(query);

        //then
        assertThat(userSignUpDto)
                .extracting("userId", "password", "name", "email")
                .contains("user1", "1234", "test", "test@naver.com");
    }

}
