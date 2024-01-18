package http.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GeneralHeader {
    private final Map<String, String> generalHeaders = new HashMap<>();

    private static final ArrayList<String> GENERAL_HEADER_FIELDS = new ArrayList<>();

    static {
        GENERAL_HEADER_FIELDS.add("Cache-Control");
        GENERAL_HEADER_FIELDS.add("Connection");
        GENERAL_HEADER_FIELDS.add("Date");
        GENERAL_HEADER_FIELDS.add("Pragma");
        GENERAL_HEADER_FIELDS.add("Trailer");
        GENERAL_HEADER_FIELDS.add("Transfer-Encoding");
        GENERAL_HEADER_FIELDS.add("Upgrade");
        GENERAL_HEADER_FIELDS.add("Via");
        GENERAL_HEADER_FIELDS.add("Warning");
    }

    public Map<String, String> getGeneralHeaders() {
        return generalHeaders;
    }

    public void addGeneralHeader(String key, String value) {
        if (GENERAL_HEADER_FIELDS.contains(key)) {
            generalHeaders.put(key, value);
        }
    }

    public boolean checkGeneralHeader(String key) {
        return GENERAL_HEADER_FIELDS.contains(key);
    }
}
