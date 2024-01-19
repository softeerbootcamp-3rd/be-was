package common.binder;

import java.lang.reflect.Field;

import static common.util.Util.splitParamters;
import static common.util.Util.splitParameter;

public class Binder {

    public static <T> T bindQueryStringToObject(String queryString, Class<T> dtoClass) throws Exception {
        T dto = dtoClass.getDeclaredConstructor().newInstance();
        String[] params = splitParamters(queryString);

        for (String param : params) {
            String[] keyValue = splitParameter(param);
            String fieldName = keyValue[0];
            String fieldValue = keyValue[1];

            Field field = dtoClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(dto, fieldValue);
        }

        return dto;
    }
}
