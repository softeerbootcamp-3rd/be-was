package dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserDtoTest {

    @DisplayName("쿼리 스트링 문자열을 UserDto로 변환할 수 있다.")
    @Test
    void userDtoFrom(){
        //given
        String query = "userId=user1&password=1234&name=test&email=test@naver.com";

        //when
        UserDto userDto = UserDto.from(query);

        //then
        assertThat(userDto)
                .extracting("userId", "password", "name", "email")
                .contains("user1", "1234", "test", "test@naver.com");
    }

    @DisplayName("하나라도 필드가 없는 경우 예외가 발생한다.")
    @Test
    void userDtoFromWithNoEmail(){
        //given
        String query = "userId=user1&password=1234&name=test&email=";

        //when //then
        assertThatThrownBy(() -> UserDto.from(query))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("email은 필수입니다.");
    }

}