package controller.dto;

import java.util.Map;

public class InputData {

    private final Map<String, String> data;

    public InputData(Map<String, String> data) {
        this.data = data;
    }

    public String get(String key) {
        return data.get(key);
    }
}
