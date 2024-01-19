package util.http;

import java.net.URI;
import java.util.*;
import java.util.function.BiConsumer;

import util.Assert;
import util.MultiValueMap;
import util.MultiValueMapAdapter;
import util.StringUtils;

public class HttpHeaders implements MultiValueMap<String, String> {
    public static final String ACCEPT = "Accept";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String LOCATION = "Location";

    final MultiValueMap<String, String> headers;

    public HttpHeaders() {
        this(new MultiValueMapAdapter<>(new LinkedHashMap<>()));
    }

    public HttpHeaders(MultiValueMap<String, String> headers) {
        Assert.notNull(headers, "MultiValueMap must not be null");
        this.headers = headers;
    }

    public void setAccept(List<MediaType> acceptableMediaTypes) {
        set(ACCEPT, MediaType.toString(acceptableMediaTypes));
    }

    public List<MediaType> getAccept() {
        return MediaType.parseMediaTypes(get(ACCEPT));
    }


    public void setContentLength(long contentLength) {
        set(CONTENT_LENGTH, Long.toString(contentLength));
    }

    public long getContentLength() {
        String value = getFirst(CONTENT_LENGTH);
        return (value != null ? Long.parseLong(value) : -1);
    }

    public void setContentType(MediaType mediaType) {
        if (mediaType != null) {
            Assert.isTrue(!mediaType.isWildcardType(), "Content-Type cannot contain wildcard type '*'");
            Assert.isTrue(!mediaType.isWildcardSubtype(), "Content-Type cannot contain wildcard subtype '*'");
            set(CONTENT_TYPE, mediaType.toString());
        }
        else {
            remove(CONTENT_TYPE);
        }
    }

    public MediaType getContentType() {
        String value = getFirst(CONTENT_TYPE);
        return (StringUtils.hasLength(value) ? MediaType.parseMediaType(value) : null);
    }

    public void setLocation(URI location) {
        setOrRemove(LOCATION, (location != null ? location.toASCIIString() : null));
    }
    public URI getLocation() {
        String value = getFirst(LOCATION);
        return (value != null ? URI.create(value) : null);
    }

    public void clearContentHeaders() {
        this.headers.remove(HttpHeaders.CONTENT_LENGTH);
        this.headers.remove(HttpHeaders.CONTENT_TYPE);
    }

    protected String getFieldValues(String headerName) {
        List<String> headerValues = get(headerName);
        return (headerValues != null ? toCommaDelimitedString(headerValues) : null);
    }

    protected String toCommaDelimitedString(List<String> headerValues) {
        StringJoiner joiner = new StringJoiner(", ");
        for (String val : headerValues) {
            if (val != null) {
                joiner.add(val);
            }
        }
        return joiner.toString();
    }

    private void setOrRemove(String headerName, String headerValue) {
        if (headerValue != null) {
            set(headerName, headerValue);
        }
        else {
            remove(headerName);
        }
    }


    // MultiValueMap implementation
    @Override
    public String getFirst(String headerName) {
        return this.headers.getFirst(headerName);
    }

    @Override
    public void add(String headerName, String headerValue) {
        this.headers.add(headerName, headerValue);
    }

    @Override
    public void addAll(String key, List<? extends String> values) {
        this.headers.addAll(key, values);
    }

    @Override
    public void addAll(MultiValueMap<String, String> values) {
        this.headers.addAll(values);
    }

    @Override
    public void set(String headerName, String headerValue) {
        this.headers.set(headerName, headerValue);
    }

    @Override
    public void setAll(Map<String, String> values) {
        this.headers.setAll(values);
    }

    // Map implementation

    @Override
    public int size() {
        return this.headers.size();
    }

    @Override
    public boolean isEmpty() {
        return this.headers.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.headers.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.headers.containsValue(value);
    }

    @Override
    public List<String> get(Object key) {
        return this.headers.get(key);
    }

    @Override
    public List<String> put(String key, List<String> value) {
        return this.headers.put(key, value);
    }

    @Override
    public List<String> remove(Object key) {
        return this.headers.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<String>> map) {
        this.headers.putAll(map);
    }

    @Override
    public void clear() {
        this.headers.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.headers.keySet();
    }

    @Override
    public Collection<List<String>> values() {
        return this.headers.values();
    }

    @Override
    public Set<Map.Entry<String, List<String>>> entrySet() {
        return this.headers.entrySet();
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super List<String>> action) {
        this.headers.forEach(action);
    }

    @Override
    public List<String> putIfAbsent(String key, List<String> value) {
        return this.headers.putIfAbsent(key, value);
    }


    @Override
    public int hashCode() {
        return this.headers.hashCode();
    }

}
