package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.view.InternalResourceView;
import webserver.view.RedirectView;
import webserver.view.View;

import java.nio.file.Paths;

public class ViewResolver {
    private static final Logger logger = LoggerFactory.getLogger(dispatcherServlet.class);

    public static View resolve(String viewName) {
        if (isRedirect(viewName)){
            return new RedirectView(viewName);
        }
        else if(isTemplate(viewName)||isStatic(viewName)){
            return new InternalResourceView(getAbsolutePath(viewName));
        }

        return new InternalResourceView(getAbsolutePath(viewName) + ".html");
    }

    private static boolean isRedirect(String viewName) {
        return viewName.startsWith("redirect:");
    }

    public static Boolean isTemplate(String url){
        return url.endsWith(".html");
    }
    public static boolean isStatic(String url) {
        return url.matches(".*/(?:css|fonts|images|js)/.*|.*\\.(?:ico|png|jpg)");
    }

    public static String getAbsolutePath(String viewPath){
        if(viewPath.endsWith(".html")){
            return Paths.get(System.getProperty("user.dir"), "src/main/resources/templates").toString()+"/"+viewPath;
        }
        else{
            return Paths.get(System.getProperty("user.dir"), "src/main/resources/static").toString() + viewPath;
        }
    }
}
