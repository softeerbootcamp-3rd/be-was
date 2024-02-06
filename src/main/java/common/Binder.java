package common;

import common.util.Util;

import java.lang.reflect.Field;

import static http.RequestParser.PARAMETERS_DELIMITER;
import static http.RequestParser.PARAMETER_DELIMITER;


public class Binder {

    public static <T> T bindQueryStringToObject(String queryString, Class<T> dtoClass) throws Exception {
        T dto = dtoClass.getDeclaredConstructor().newInstance();
        String[] params = queryString.split(PARAMETERS_DELIMITER);

        for (String param : params) {
            String[] keyValue = param.split(PARAMETER_DELIMITER);
            String fieldName = keyValue[0];
            String fieldValue = Util.decode(keyValue[1]);

            Field field = dtoClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(dto, fieldValue);
        }

        return dto;
    }
}
