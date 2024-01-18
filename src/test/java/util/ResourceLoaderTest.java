package util;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class ResourceLoaderTest {

    static Stream<Object[]> provideUrl() {
        return Stream.of(
                new Object[]{"/", "API"},
                new Object[]{"/index.html", "FILE"},
                new Object[]{"/user/form.html", "FILE"},
                new Object[]{"/user/list.html", "FILE"},
                new Object[]{"/user/login.html", "FILE"},
                new Object[]{"/user/profile.html", "FILE"},
                new Object[]{"/user/create?userId=asdf&password=asdf&name=asdf&email=asdf", "API"}
        );
    }

    @ParameterizedTest
    @MethodSource("provideUrl")
    @DisplayName("유효한 입력")
    public void getResourceType_ValidUrl(String path, String expect) {
        // Given

        // When
        String actual = ResourceLoader.getResourceType(path);

        // Then
        assertThat(actual).isEqualTo(expect);

    }

    static Stream<Object[]> provideUrlWithEmail() {
        return Stream.of(
                new Object[]{"/user/create?userId=asdf&password=asdf&name=asdf&email=asdf@gmail.com", "API"},
                new Object[]{"/user/create?userId=asdf&password=asdf&name=asdf&email=asdf@aa.bb.cc", "API"}
        );
    }

    @ParameterizedTest
    @MethodSource("provideUrlWithEmail")
    @DisplayName("이메일('~~.com') 입력")
    public void getResourceType_ValidUrl_Email(String path, String expect) {
        // Given

        // When
        String actual = ResourceLoader.getResourceType(path);

        // Then
        assertThat(actual).isEqualTo(expect);

    }
}
