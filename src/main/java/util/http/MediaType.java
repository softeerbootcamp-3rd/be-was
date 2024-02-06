package util.http;

import java.util.*;

import util.*;

public class MediaType extends MimeType {
    public static final MediaType ALL;
    public static final String ALL_VALUE = "*/*";
    public static final MediaType TEXT_PLAIN;
    public static final String TEXT_PLAIN_VALUE = "text/plain";
    public static final MediaType TEXT_HTML;
    public static final String TEXT_HTML_VALUE = "text/html";
    public static final MediaType TEXT_CSS;
    public static final String TEXT_CSS_VALUE = "text/html";
    public static final MediaType TEXT_JAVASCRIPT;
    public static final String TEXT_JAVASCRIPT_VALUE = "text/javascript";
    public static final MediaType IMAGE_PNG;
    public static final String IMAGE_PNG_VALUE = "image/png";
    public static final MediaType IMAGE_JPEG;
    public static final String IMAGE_JPEG_VALUE = "image/jpeg";
    public static final MediaType IMAGE_GIF;
    public static final String IMAGE_GIF_VALUE = "image/gif";
    public static final MediaType IMAGE_SVG_XML;
    public static final String IMAGE_SVG_XML_VALUE = "image/svg+xml";
    public static final MediaType IMAGE_X_ICON;
    public static final String IMAGE_X_ICON_VALUE = "image/x-icon";
    public static final MediaType APPLICATION_JSON;
    public static final String APPLICATION_JSON_VALUE = "application/json";
    public static final MediaType APPLICATION_PDF;
    public static final String APPLICATION_PDF_VALUE = "application/pdf";
    public static final MediaType APPLICATION_VND_MS_FONTOBJECT;
    public static final String APPLICATION_VND_MS_FONTOBJECT_VALUE = "application/vnd.ms-fontobject";
    public static final MediaType FONT_TTF;
    public static final String FONT_TTF_VALUE = "font/ttf";
    public static final MediaType FONT_WOFF;
    public static final String FONT_WOFF_VALUE = "font/woff";
    public static final MediaType FONT_WOFF2;
    public static final String FONT_WOFF2_VALUE = "font/woff2";
    private static final Map<String, MediaType> MEDIA_TYPE = new HashMap<>();


    static {
        ALL = new MediaType(MimeType.WILDCARD_TYPE, MimeType.WILDCARD_TYPE);
        TEXT_PLAIN = new MediaType("text", "plain");
        TEXT_HTML = new MediaType("text", "html");
        TEXT_CSS = new MediaType("text", "css");
        TEXT_JAVASCRIPT = new MediaType("text", "javascript");
        IMAGE_PNG = new MediaType("image", "png");
        IMAGE_JPEG = new MediaType("image", "jpeg");
        IMAGE_GIF = new MediaType("image", "gif");
        IMAGE_SVG_XML = new MediaType("image", "svg+xml");
        IMAGE_X_ICON =  new MediaType("image", "x-icon");
        APPLICATION_JSON = new MediaType("application", "json");
        APPLICATION_PDF = new MediaType("application", "pdf");
        APPLICATION_VND_MS_FONTOBJECT = new MediaType("application", "vnd.ms-fontobject");
        FONT_TTF = new MediaType("font", "ttf");
        FONT_WOFF = new MediaType("font", "woff");
        FONT_WOFF2 = new MediaType("font", "woff2");

        MEDIA_TYPE.put("html", TEXT_HTML);
        MEDIA_TYPE.put("css", TEXT_CSS);
        MEDIA_TYPE.put("js", TEXT_JAVASCRIPT);
        MEDIA_TYPE.put("png", IMAGE_PNG);
        MEDIA_TYPE.put("jpg", IMAGE_JPEG);
        MEDIA_TYPE.put("jpe", IMAGE_JPEG);
        MEDIA_TYPE.put("jpeg", IMAGE_JPEG);
        MEDIA_TYPE.put("gif", IMAGE_GIF);
        MEDIA_TYPE.put("svg", IMAGE_SVG_XML);
        MEDIA_TYPE.put("ico", IMAGE_X_ICON);
        MEDIA_TYPE.put("json", APPLICATION_JSON);
        MEDIA_TYPE.put("pdf", APPLICATION_PDF);
        MEDIA_TYPE.put("eot", APPLICATION_VND_MS_FONTOBJECT);
        MEDIA_TYPE.put("ttf", FONT_TTF);
        MEDIA_TYPE.put("woff", FONT_WOFF);
        MEDIA_TYPE.put("woff2", FONT_WOFF2);
    }


    public MediaType(String type) {
        super(type);
    }

    public MediaType(String type, String subtype) {
        super(type, subtype);
    }

    public MediaType(MimeType mimeType) {
        super(mimeType);
    }

    public static MediaType getContentType(HttpRequest httpRequest) {
        String accept = httpRequest.getHeader(HttpHeaders.ACCEPT);
        if (ALL_VALUE.equals(accept)) {
            return MEDIA_TYPE.getOrDefault(ResourceUtils.getExtension(httpRequest.getPath()), TEXT_PLAIN);
        }
       return parseMediaType(accept);
    }

    public static MediaType parseMediaType(String mediaType) {
        MimeType type;
        try {
            type = MimeTypeUtils.parseMimeType(mediaType);
        }
        catch (RuntimeException ex) {
            throw new RuntimeException();
        }
        try {
            return new MediaType(type);
        }
        catch (RuntimeException ex) {
            throw new RuntimeException();
        }
    }

    public static List<MediaType> parseMediaTypes(String mediaTypes) {
        if (!StringUtils.hasLength(mediaTypes)) {
            return Collections.emptyList();
        }
        // Avoid using java.util.stream.Stream in hot paths
        List<String> tokenizedTypes = MimeTypeUtils.tokenize(mediaTypes);
        List<MediaType> result = new ArrayList<>(tokenizedTypes.size());
        for (String type : tokenizedTypes) {
            if (StringUtils.hasText(type)) {
                result.add(parseMediaType(type));
            }
        }
        return result;
    }


    public static List<MediaType> parseMediaTypes(List<String> mediaTypes) {
        if (CollectionUtils.isEmpty(mediaTypes)) {
            return Collections.emptyList();
        }
        else if (mediaTypes.size() == 1) {
            return parseMediaTypes(mediaTypes.get(0));
        }
        else {
            List<MediaType> result = new ArrayList<>(8);
            for (String mediaType : mediaTypes) {
                result.addAll(parseMediaTypes(mediaType));
            }
            return result;
        }
    }

    public static String toString(Collection<MediaType> mediaTypes) {
        return MimeTypeUtils.toString(mediaTypes);
    }
}
