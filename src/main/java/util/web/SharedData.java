package util.web;

import com.google.common.base.Strings;
import constant.ParamType;
import model.User;
import webserver.HttpRequest;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedData {
    public static ThreadLocal<HttpRequest> request = new ThreadLocal<>();
    public static ThreadLocal<User> requestUser = new ThreadLocal<>();
    public static ThreadLocal<Map<String, String>> pathParams = new ThreadLocal<>();

    public static <T> T getParamData(String key, Class<T> type) {
        String resultString = request.get().getParamMap().get(key);
        if (Strings.isNullOrEmpty(resultString)) {
            return null;
        }

        // 알맞은 타입으로 캐스팅해서 리턴
        ParamType paramType = ParamType.getByClass(type);
        Object castedValue = paramType.map(resultString);
        return type.cast(castedValue);
    }

    public static <T> T getParamDataNotEmpty(String key, Class<T> type) {
        T result = getParamData(key, type);
        if (result == null)
            throw new InvalidParameterException("Parameter " + key + " is empty");
        return result;
    }

    public static <T> T getParamDataOrElse(String key, Class<T> type, T defaultValue) {
        T result = getParamData(key, type);
        if (result == null)
            return defaultValue;
        return result;
    }
}
