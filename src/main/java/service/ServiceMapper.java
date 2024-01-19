package service;

import java.util.HashMap;
import java.util.Map;
import service.user.OneUserSearchService;
import service.user.TotalUserSearchService;
import service.user.UserCreateService;

public class ServiceMapper {

    private static final Map<String, Class<? extends Service>> mapping = new HashMap<>();

    static {
        // URL과 서비스 클래스의 매핑 정보를 초기화
        mapping.put("/user/create", UserCreateService.class);
        mapping.put("/user/total", TotalUserSearchService.class);
        mapping.put("/user/one", OneUserSearchService.class);
        // 추가적인 서비스 매핑 정보를 필요에 따라 추가
    }

    public static Class<? extends Service> getServiceClass(String url) {
        // URL을 기반으로 서비스 클래스를 찾아 반환
        return mapping.get(url);
    }
}
