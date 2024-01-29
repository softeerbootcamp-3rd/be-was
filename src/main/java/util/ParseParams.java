package util;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ParseParams {
    private Map<String, String> paramMap;

    public ParseParams(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public static ParseParams from(String query) {
        return new ParseParams(
                Arrays.stream(query.split("&"))
                        .map(Param::from)
                        .collect(Collectors.toMap(Param::getKey, Param::getValue)));
    }
}
