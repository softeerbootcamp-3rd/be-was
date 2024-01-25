package util;

import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class ParserTest {

    @ParameterizedTest
    @MethodSource("valid_json__parameters")
    @DisplayName("유효한 body를 매개변수로 받아 json을 파싱하면 User 객체를 반환하는지 확인")
    void json_parser_test(String body) throws Exception {
        User user = Parser.jsonParser(User.class, body);

        Assertions.assertThat(user)
                .extracting("userId", "password", "name", "email")
                .contains("json", "1234", "json", "json@json.com");
    }

    @ParameterizedTest
    @MethodSource("invalid_json_parameters")
    @DisplayName("유효하지 않은 body를 매개변수로 받아 json을 파싱하면 예외를 던지는지 확인")
    void invalid_body_json_parser_test(String body) {
        Assertions.assertThatThrownBy(() -> Parser.jsonParser(User.class, body))
                .isInstanceOf(Exception.class);
    }

    @ParameterizedTest
    @MethodSource("invalid_json_field_parameters")
    @DisplayName("유효하지 않은 body의 필드를 매개변수로 받아 json을 파싱하면 예외를 던지는지 확인")
    void invalid_field_json_parser_test(String body) {
        Assertions.assertThatThrownBy(() -> Parser.jsonParser(User.class, body))
                .isInstanceOf(Exception.class);
    }

    private static Stream<Arguments> valid_json__parameters() {
        return Stream.of(
                Arguments.of("userId=json&password=1234&name=json&email=json@json.com"),
                Arguments.of("password=1234&userId=json&name=json&email=json@json.com") // 순서가 바뀌어도 파싱이 잘 되는지 확인
        );
    }

    private static Stream<Arguments> invalid_json_parameters() {
        return Stream.of(
                Arguments.of("userId=json&password=1234&name=json"), // email이 없는 경우
                Arguments.of("userId=json&password=1234&email=json@json.com"), // name이 없는 경우
                Arguments.of("password=1234&name=json&email=json@json.com"), // userId가 없는 경우
                Arguments.of("userId=json&name=json&email=json@json.com") // password가 없는 경우
        );
    }

    private static Stream<Arguments> invalid_json_field_parameters() {
        return Stream.of(
                Arguments.of("Id=json&password=1234&name=json&email=json@json.com"), // userId가 아닌 Id로 시작하는 경우
                Arguments.of("userId=json&userPassword=1234&name=json&email=json@json.com"), // password가 아닌 userPassword로 시작하는 경우
                Arguments.of("userId=json&password=1234&userName=json&email=json@json.com"), // name이 아닌 userName으로 시작하는 경우
                Arguments.of("userId=json&password=1234&name=json&userEmail=json@json.com") // email이 아닌 userEmail로 시작하는 경우
        );
    }
}