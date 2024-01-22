package model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryParams {
    private Map<String, String> paramMap;

    public QueryParams(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public static QueryParams from(String query) {
        return new QueryParams(
                Arrays.stream(query.split("&"))
                        .map(QueryParam::from)
                        .collect(Collectors.toMap(QueryParam::getKey, QueryParam::getValue)));
    }
}
