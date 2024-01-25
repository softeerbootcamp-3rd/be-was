package constants;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ContentType {

    HTML(".html", "text/html"),
    CSS(".css", "text/css"),
    JS(".js", "text/javascript"),
    ICO(".ico", "image/x-icon"),
    TTF(".ttf", "font/ttf"),
    WOFF(".woff", "font/woff"),
    WOFF2(".woff2", "font/woff2"),
    SVG(".svg", "image/svg+xml"),
    PNG(".png", "image/png"),
    EOT(".eot", "application/vnd.ms-fontobject");

    private final String extension;
    private final String contentType;

    /**
     * 확장자명과 열거체의 이름을 각각 키와 값으로 갖는 자료구조입니다.
     */
    private static final Map<String, String> contentTypes = Collections.unmodifiableMap(
            Stream.of(values())
                    .collect(Collectors.toMap(ContentType::getExtension, ContentType::name)));

    ContentType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return this.extension;
    }

    public String getContentType() {
        return this.contentType;
    }

    /**
     * 파일 확장자명에서 컨텐츠 타입을 찾습니다.
     *
     * <p> 찾지 못한다면 오류를 발생시킵니다.
     *
     * @param url 요청 타겟
     * @return 확장자명에 앎자는 컨텐츠 타입
     * @throws NullPointerException 컨텐츠 타입을 찾지 못한 경우 발생
     */
    public static String findContentType(String url) throws NullPointerException {
        String extension = url.substring(url.lastIndexOf("."));
        try {
            String type = contentTypes.get(extension);
            ContentType contentType = ContentType.valueOf(type);
            return contentType.getContentType() + "; charset=utf-8";
        } catch (NullPointerException e) {
            throw new NullPointerException("no such content type.");
        }
    }
}
