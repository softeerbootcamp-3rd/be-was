package model.http;

import exception.BadRequestException;

public enum ContentType {
    // TEXT 타입
    PLAIN("text/plain", "plain"),
    CSS("text/css", "css"),
    HTML("text/html", "html"),
    JAVASCRIPT("application/javascript", "js"),
    WOFF("application/x-font-woff", "woff"),
    WOFF2("application/x-font/woff2", "woff2"),
    TTF("application/x-font/ttf", "ttf"),
    ICO("image/x-icon", "ico"),
    PNG("image/png", "png"),
    JPG("image/jpg", "jpg");
    private final String type;
    private final String extension;

    ContentType(String type, String extension) {
        this.type = type;
        this.extension = extension;
    }

    public String getType() {
        return type;
    }

    public String getExtension() {
        return extension;
    }

    static public ContentType getContentTypeByAccept(String accept){
        for (ContentType contentType : ContentType.values()) {
            if(accept.contains(contentType.getType())){
                return contentType;
            }
        }
        throw new BadRequestException("호환되는 Content-Type이 존재하지 않습니다.");
    }
    static public ContentType getContentTypeByExtension(String extension){
        for (ContentType contentType : ContentType.values()) {
            if(contentType.getExtension().contains(extension)){
                return contentType;
            }
        }
        throw new BadRequestException("호환되는 Content-Type이 존재하지 않습니다.");
    }
}
