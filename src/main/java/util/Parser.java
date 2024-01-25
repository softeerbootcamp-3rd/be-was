package util;

import annotation.Column;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    public static <T> T jsonParser(Class<T> clazz, String body) throws Exception {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        T object = constructor.newInstance();
        Field[] fields = clazz.getDeclaredFields();

        Map<String, String> keyValueMap = parseJson(body);

        setField(object, fields, keyValueMap);
        return object;
    }

    private static Map<String, String> parseJson(String body) {

        String[] jsons = body.split("&");
        Map<String, String> keyValueMap = new HashMap<>();

        for (String s : jsons) {
            String[] keyValue = s.split("=");
            keyValueMap.put(keyValue[0], keyValue[1]);
        }

        return keyValueMap;
    }

    private static void setField(Object object, Field[] fields, Map<String, String> keyValueMap) throws Exception {
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) { // @Column 어노테이션이 있는 필드만 필드값 설정
                String value = keyValueMap.get(field.getName());

                if (value == null) throw new Exception("필드명이 유효하지 않습니다.");

                field.setAccessible(true);
                field.set(object, value);
            }
        }
    }
}
