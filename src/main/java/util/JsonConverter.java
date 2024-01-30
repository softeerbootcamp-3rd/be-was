package util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonConverter {

    public static <T> String convertObjectToJson(T object) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        if (object == null) {
            return "";
        }

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            try {
                if (isListType(field.getType())) {
                    // 만약 필드의 타입이 List라면 해당 List를 JSON 형식으로 변환하여 추가
                    List<?> listValue = (List<?>) field.get(object);
                    jsonBuilder.append("\"").append(field.getName()).append("\":").append(convertListToJson(listValue)).append(",");
                } else {
                    jsonBuilder.append("\"").append(field.getName()).append("\":");
                    Object fieldValue = field.get(object);
                    // 값이 없으면 'null'로 추가
                    if (fieldValue == null) {
                        jsonBuilder.append("null,");
                    } else {
                        jsonBuilder.append("\"").append(fieldValue).append("\",");
                    }
                }
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

    public static <T> String convertListToJson(List<T> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }

        StringBuilder jsonBuilder = new StringBuilder("[");
        for (T item : list) {
            jsonBuilder.append(convertObjectToJson(item)).append(",");
        }

        // 마지막 쉼표 제거
        if (jsonBuilder.charAt(jsonBuilder.length() - 1) == ',') {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }

        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }

    private static boolean isListType(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

}