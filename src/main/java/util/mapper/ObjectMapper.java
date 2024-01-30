package util.mapper;

import com.google.common.base.Strings;
import constant.ParamType;
import util.web.SharedData;
import webserver.HttpRequest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectMapper {

    public static <T> T mapRequestParam(String paramName, Class<T> clazz) {
        HttpRequest request = SharedData.request.get();
        String requestParam = request.getParamMap().get(paramName);
        if (Strings.isNullOrEmpty(requestParam))
            return null;
        ParamType paramType = ParamType.getByClass(clazz);
        Object result = paramType.map(requestParam);
        return clazz.cast(result);
    }

    public static <T> T mapToClass(Map<String, String> mapInfo, Class<T> clazz)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        T result = clazz.getDeclaredConstructor().newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldValue = mapInfo.get(field.getName());
            if (fieldValue != null) {
                ParamType paramType = ParamType.getByClass(field.getType());
                field.set(result, paramType.map(fieldValue));
            }
        }
        return result;
    }

    public static <T> T jsonToClass(String json, Class<T> clazz)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T result = clazz.getDeclaredConstructor().newInstance();
        parseJsonObject(json, result);
        return result;
    }

    private static void parseJsonObject(String json, Object obj) throws IllegalAccessException {
        Pattern pattern = Pattern.compile("\"(\\w+)\":\\s*(\"[^\"]+\"|\\d+|\\{.*?\\}|\\[.*?\\])");
        Matcher matcher = pattern.matcher(json);

        while (matcher.find()) {
            String fieldName = matcher.group(1);
            String fieldValue = matcher.group(2);

            try {
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                if (field.getType() == String.class) {
                    field.set(obj, fieldValue.replaceAll("\"", ""));
                } else if (field.getType() == int.class || field.getType() == Integer.class) {
                    field.set(obj, Integer.parseInt(fieldValue));
                } else if (fieldValue.startsWith("{")) {
                    // 객체
                    Class<?> nestedType = field.getType();
                    Object nestedObj = nestedType.getDeclaredConstructor().newInstance();
                    parseJsonObject(fieldValue, nestedObj);
                    field.set(obj, nestedObj);
                } else if (fieldValue.startsWith("[")) {
                    // 배열
                    Object[] array = parseJsonArray(fieldValue, field.getType().getComponentType());
                    field.set(obj, array);
                }
            } catch (NoSuchFieldException ignored) {

            } catch (InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Object[] parseJsonArray(String jsonArray, Class<?> clazz)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String[] elements = jsonArray.substring(1, jsonArray.length() - 1).split(",");
        Object[] array = (Object[]) java.lang.reflect.Array.newInstance(clazz, elements.length);

        for (int i = 0; i < elements.length; i++) {
            elements[i] = elements[i].trim();
            if (elements[i].startsWith("{")) {
                // 객체
                Object nestedObj = clazz.getDeclaredConstructor().newInstance();
                parseJsonObject(elements[i], nestedObj);
                array[i] = nestedObj;
            } else {
                String elementString = elements[i].replaceAll("(?<!\\\\)\"", "")
                        .replaceAll("\\\\\"", "\"");
                ParamType paramType = ParamType.getByClass(clazz);
                array[i] = paramType.map(elementString);
            }
        }
        return array;
    }
}
