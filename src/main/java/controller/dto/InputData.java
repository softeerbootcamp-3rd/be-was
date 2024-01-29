package controller.dto;

import java.util.Map;

public class InputData {

    private final Map<String, String> data;
    private final String sessionId;

    public InputData(Map<String, String> data, String sessionId) {
        this.data = data;
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String get(String key) {
        return data.get(key);
    }
}
