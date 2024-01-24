package webserver.container;

import common.http.request.HttpRequest;
import common.http.request.StaticResourceExtension;

public class Dispatcher {

    private Dispatcher() {
    }

    private static class SingletonHelper {

        private static final Dispatcher SINGLETON = new Dispatcher();
    }

    public static Dispatcher getInstance() {
        return SingletonHelper.SINGLETON;
    }

    public void process(HttpRequest request) throws Exception {
        boolean isStaticRequest = isStaticResource(
            request.getHttpRequestStartLine().getRequestTarget());
        if (isStaticRequest) {
            ViewResolver.getInstance()
                .findModelAndView(request.getHttpRequestStartLine().getRequestTarget());
        } else {
            HandlerMapping.getInstance().process(request);
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
