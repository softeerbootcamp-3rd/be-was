package util;

import config.HTTPRequest;

@FunctionalInterface
public interface PageBuilder {
    byte[] build(HTTPRequest request) throws Exception;
}
