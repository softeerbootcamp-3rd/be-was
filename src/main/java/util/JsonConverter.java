package util;

import java.lang.reflect.Field;

public class JsonConverter {

    public static <T> String convertObjectToJson(T object) {

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        // 객체의 각 필드를 처리
        if (object == null) {
            jsonBuilder.append("}");
            return jsonBuilder.toString();
        }

        Field[] fields = object.getClass().getFields();
        for (Field field : fields) {
            field.setAccessible(true); // 필드 접근 권한 설정

            try {
                // 필드 이름과 값을 JSON 형식으로 추가
                jsonBuilder.append("\"").append(field.getName()).append("\":\"").append(field.get(object)).append("\",");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // 마지막 쉼표 제거
        if (jsonBuilder.charAt(jsonBuilder.length() - 1) == ',') {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }

        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

}
