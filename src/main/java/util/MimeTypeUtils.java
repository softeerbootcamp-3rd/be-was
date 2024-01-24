package util;

import java.util.*;

public abstract class MimeTypeUtils {
    public static final MimeType ALL;
    public static final String ALL_VALUE = "*/*";
    public static final MimeType APPLICATION_GRAPHQL;
    public static final String APPLICATION_GRAPHQL_VALUE = "application/graphql+json";
    public static final MimeType APPLICATION_JSON;
    public static final String APPLICATION_JSON_VALUE = "application/json";
    public static final MimeType APPLICATION_OCTET_STREAM;
    public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";
    public static final MimeType APPLICATION_XML;
    public static final String APPLICATION_XML_VALUE = "application/xml";
    public static final MimeType IMAGE_GIF;
    public static final String IMAGE_GIF_VALUE = "image/gif";
    public static final MimeType IMAGE_JPEG;
    public static final String IMAGE_JPEG_VALUE = "image/jpeg";
    public static final MimeType IMAGE_PNG;
    public static final String IMAGE_PNG_VALUE = "image/png";
    public static final MimeType TEXT_HTML;
    public static final String TEXT_HTML_VALUE = "text/html";
    public static final MimeType TEXT_PLAIN;
    public static final String TEXT_PLAIN_VALUE = "text/plain";
    public static final MimeType TEXT_XML;
    public static final String TEXT_XML_VALUE = "text/xml";

    private static final ConcurrentLruCache<String, MimeType> cachedMimeTypes =
            new ConcurrentLruCache<>(64, MimeTypeUtils::parseMimeTypeInternal);


    static {
        // Not using "parseMimeType" to avoid static init cost
        ALL = new MimeType(MimeType.WILDCARD_TYPE, MimeType.WILDCARD_TYPE);
        APPLICATION_GRAPHQL = new MimeType("application", "graphql+json");
        APPLICATION_JSON = new MimeType("application", "json");
        APPLICATION_OCTET_STREAM = new MimeType("application", "octet-stream");
        APPLICATION_XML = new MimeType("application", "xml");
        IMAGE_GIF = new MimeType("image", "gif");
        IMAGE_JPEG = new MimeType("image", "jpeg");
        IMAGE_PNG = new MimeType("image", "png");
        TEXT_HTML = new MimeType("text", "html");
        TEXT_PLAIN = new MimeType("text", "plain");
        TEXT_XML = new MimeType("text", "xml");
    }

    public static String toString(Collection<? extends MimeType> mimeTypes) {
        StringBuilder builder = new StringBuilder();
        for (Iterator<? extends MimeType> iterator = mimeTypes.iterator(); iterator.hasNext();) {
            MimeType mimeType = iterator.next();
            mimeType.appendTo(builder);
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public static MimeType parseMimeType(String mimeType) {
        if (!StringUtils.hasLength(mimeType)) {
            throw new RuntimeException();
        }
        // do not cache multipart mime types with random boundaries
        if (mimeType.startsWith("multipart")) {
            return parseMimeTypeInternal(mimeType);
        }
        return cachedMimeTypes.get(mimeType);
    }

    private static MimeType parseMimeTypeInternal(String mimeType) {
        int index = mimeType.indexOf(';');
        String fullType = (index >= 0 ? mimeType.substring(0, index) : mimeType).trim();
        if (fullType.isEmpty()) {
            throw new RuntimeException();
        }

        // java.net.HttpURLConnection returns a *; q=.2 Accept header
        if (MimeType.WILDCARD_TYPE.equals(fullType)) {
            fullType = "*/*";
        }
        int subIndex = fullType.indexOf('/');
        if (subIndex == -1) {
            throw new RuntimeException();
        }
        if (subIndex == fullType.length() - 1) {
            throw new RuntimeException();
        }
        String type = fullType.substring(0, subIndex);
        String subtype = fullType.substring(subIndex + 1);
        if (MimeType.WILDCARD_TYPE.equals(type) && !MimeType.WILDCARD_TYPE.equals(subtype)) {
            throw new RuntimeException();
        }

        Map<String, String> parameters = null;
        do {
            int nextIndex = index + 1;
            boolean quoted = false;
            while (nextIndex < mimeType.length()) {
                char ch = mimeType.charAt(nextIndex);
                if (ch == ';') {
                    if (!quoted) {
                        break;
                    }
                } else if (ch == '"') {
                    quoted = !quoted;
                }
                nextIndex++;
            }
            String parameter = mimeType.substring(index + 1, nextIndex).trim();
            if (!parameter.isEmpty()) {
                if (parameters == null) {
                    parameters = new LinkedHashMap<>(4);
                }
                int eqIndex = parameter.indexOf('=');
                if (eqIndex >= 0) {
                    String attribute = parameter.substring(0, eqIndex).trim();
                    String value = parameter.substring(eqIndex + 1).trim();
                    parameters.put(attribute, value);
                }
            }
            index = nextIndex;
        }
        while (index < mimeType.length());

        try {
            return new MimeType(type, subtype);
        } catch (RuntimeException ex) {
            throw new RuntimeException();
        }
    }

    public static List<String> tokenize(String mimeTypes) {
        if (!StringUtils.hasLength(mimeTypes)) {
            return Collections.emptyList();
        }
        List<String> tokens = new ArrayList<>();
        boolean inQuotes = false;
        int startIndex = 0;
        int i = 0;
        while (i < mimeTypes.length()) {
            switch (mimeTypes.charAt(i)) {
                case '"':
                    inQuotes = !inQuotes;
                case ',':
                    if (!inQuotes) {
                        tokens.add(mimeTypes.substring(startIndex, i));
                        startIndex = i + 1;
                    }
                case '\\':
                    i++;
            }
            i++;
        }
        tokens.add(mimeTypes.substring(startIndex));
        return tokens;
    }
}

