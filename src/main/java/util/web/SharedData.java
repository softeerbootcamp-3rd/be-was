package util.web;

import com.google.common.base.Strings;
import constant.ParamType;
import model.User;
import webserver.HttpRequest;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class SharedData {
    public static ThreadLocal<HttpRequest> request = new ThreadLocal<>();
    public static ThreadLocal<User> requestUser = new ThreadLocal<>();
    public static ThreadLocal<Map<String, String>> pathParams = new ThreadLocal<>();
    public static ThreadLocal<Map<String, Object>> commonData = new ThreadLocal<>();

    public static <T> T getParamData(String key, Class<T> type) {
        // 이미 commonData에 들어있는 값이라면 바로 return
        if (commonData.get() == null) commonData.set(new HashMap<>());
        Object result = commonData.get().get(key);
        if (type.isInstance(result)) {
            return type.cast(result);
        }

        // commonData에 없다면 paramMap에서 찾음
        String resultString = request.get().getParamMap().get(key);
        if (Strings.isNullOrEmpty(resultString)) {
            commonData.get().put(key, null);
            return null;
        }

        // 알맞은 타입으로 캐스팅해서 리턴
        ParamType paramType = ParamType.getByClass(type);
        Object castedValue = paramType.map(resultString);
        commonData.get().put(key, castedValue);
        return type.cast(castedValue);
    }

    public static <T> T getParamDataNotEmpty(String key, Class<T> type) {
        T result = getParamData(key, type);
        if (result == null)
            throw new InvalidParameterException();
        return result;
    }

    public static <T> T getParamDataOrElse(String key, Class<T> type, T defaultValue) {
        T result = getParamData(key, type);
        if (result == null)
            return defaultValue;
        return result;
    }
}
