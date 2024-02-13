package controller.dto;

import view.View;

import java.util.HashMap;
import java.util.Map;

public class OutputData {
    private final View view;
    private final Map<String, String> headerMap;

    public OutputData() {
        this.view = new View();
        this.headerMap = new HashMap<>();
    }

    public void setHeader(String key, String value) {
        headerMap.put(key, value);
    }

    public boolean headerExists() {
        return !headerMap.isEmpty();
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public View getView() {
        return view;
    }
}
