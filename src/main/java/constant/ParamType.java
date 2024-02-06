package constant;

import java.util.function.Function;

public enum ParamType {
    STRING(String.class, String::new),
    INTEGER(Integer.class, Integer::valueOf),
    LONG(Long.class, Long::valueOf),
    DOUBLE(Double.class, Double::valueOf),
    BOOLEAN(Boolean.class, Boolean::valueOf),
    ;

    private final Class<?> clazz;
    private final Function<String, ?> mappingFunction;

    ParamType(Class<?> type, Function<String, ?> mappingFunction) {
        this.clazz = type;
        this.mappingFunction = mappingFunction;
    }

    public static ParamType getByClass(Class<?> clazz) {
        for (ParamType type : values()) {
            if (type.clazz.equals(clazz)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported Type: " + clazz.getName());
    }

    public Object map(String value) {
        return mappingFunction.apply(value);
    }
}
