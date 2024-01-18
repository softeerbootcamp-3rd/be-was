package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.user.OneUserSearchService;
import service.user.TotalUserSearchService;
import service.user.UserCreateService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("ServiceMapper 클래스 테스트")
class ServiceMapperTest {

    @Test
    @DisplayName("유효한 URL에 대해 올바른 서비스 클래스를 반환하는지 테스트")
    void getServiceClass_withValidURL_returnsCorrectServiceClass() {
        // Given
        String createUserURL = "/user/create";
        String totalUserURL = "/user/total";
        String oneUserURL = "/user/one";

        // When
        Class<? extends Service> createUserClass = ServiceMapper.getServiceClass(createUserURL);
        Class<? extends Service> totalUserClass = ServiceMapper.getServiceClass(totalUserURL);
        Class<? extends Service> oneUserClass = ServiceMapper.getServiceClass(oneUserURL);

        // Then
        assertEquals(UserCreateService.class, createUserClass);
        assertEquals(TotalUserSearchService.class, totalUserClass);
        assertEquals(OneUserSearchService.class, oneUserClass);
    }

    @Test
    @DisplayName("유효하지 않은 URL에 대해 null을 반환하는지 테스트")
    void getServiceClass_withInvalidURL_returnsNull() {
        // Given
        String invalidURL = "/invalid/url";

        // When
        Class<? extends Service> invalidClass = ServiceMapper.getServiceClass(invalidURL);

        // Then
        assertNull(invalidClass);
    }
}

