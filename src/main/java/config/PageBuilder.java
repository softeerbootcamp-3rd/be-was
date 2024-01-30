package config;

@FunctionalInterface
public interface PageBuilder {
    byte[] build(HTTPRequest request) throws Exception;
}
