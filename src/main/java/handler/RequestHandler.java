package handler;

import http.request.HttpRequest;
import http.request.StaticResourceExtension;

public class RequestHandler {

    private RequestHandler() {
    }

    private static class SingletonHelper {

        private static final RequestHandler SINGLETON = new RequestHandler();
    }

    public static RequestHandler getInstance() {
        return SingletonHelper.SINGLETON;
    }

    public byte[] dispatcher(HttpRequest httpRequest) throws Exception {
        boolean isStaticRequest = isStaticResource(
            httpRequest.getHttpRequestStartLine().getRequestTarget());
        if (isStaticRequest) {
            return StaticResourceHandler.getInstance()
                .process(httpRequest.getHttpRequestStartLine().getRequestTarget());
        } else {
            return DynamicRequestHandler.getInstance().process(httpRequest);
        }
    }

    private boolean isStaticResource(String requestTarget) {
        for (StaticResourceExtension extension : StaticResourceExtension.values()) {
            if (requestTarget.contains(extension.getExtension())) {
                return true;
            }
        }
        return false;
    }
}
