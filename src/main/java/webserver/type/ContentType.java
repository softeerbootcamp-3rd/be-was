package webserver.type;

import java.util.Arrays;

public enum ContentType {
    HTML("text/html"),
    CSS("text/css"),
    JS("text/javascript"),
    ICO("image/x-icon"),
    PNG("image/png"),
    JPG("image/jpg"),
    WOFF("font/woff"),
    TTF("font/ttf"),
    SVG("font/svg");

    private final String value;

    ContentType(String value){
        this.value = value;
    }

    public String getValue() {
        return value + ";charset=utf-8";
    }

    public static ContentType findContentType(String fileExtension){
        return Arrays.stream(ContentType.values())
                .filter((type) -> type.name().equals(fileExtension.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
}
