package domain.user.query.application;

import common.utils.JsonConverter;
import java.util.List;
import java.util.Map;

public class UserResponseToJsonConverter {
    public static String userNameResponseConvertToJson(UserNameResponse userNameResponse) {
        return JsonConverter.mapToSingleObjectJson(Map.of("userName", userNameResponse.getUserName()));
    }

    public static String userInfoResponseConvertToJson(List<UserInfoResponse> userInfoResponseList) {
        return JsonConverter.mapToListObjectJson(userInfoResponseList);
    }
}
