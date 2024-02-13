package dto.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.util.Arrays;
import java.util.Map;

public enum HeaderEnum {

    HOST("Host"),
    ACCEPT("Accept"),
    CONNECTION("Connection"),
    CONTENT_LENGTH("Content-Length"),
    COOKIE("Cookie"),
    NON("");

    private String key;

    HeaderEnum(String key) {
        this.key = key;
    }

    private static final Logger logger = LoggerFactory.getLogger(HeaderEnum.class);

    // 키에 해당하는 상수 반환
    public static HeaderEnum getHeaderKey(String key) {
        return Arrays.stream(HeaderEnum.values())
                .filter(header -> header.key.equalsIgnoreCase(key))
                .findAny()
                .orElse(NON);
    }

    public Map<String, String> doParsing(Map<String, String> header, String line) {
        if(this.key.isEmpty())
            return header;
        String value = getValue(line);
        header.put(this.key, value);
        logger.debug(key + ": {}",  value);
        return header;
    }

    // value에 해당하는 값 파싱
    private String getValue(String line) {
        if(!line.contains(":"))
            return "";
        line = line.substring(line.indexOf(":")+1).strip();
        if(!line.contains(","))
            return line;
        return line.substring(0, line.indexOf(",")).strip();
    }

}
