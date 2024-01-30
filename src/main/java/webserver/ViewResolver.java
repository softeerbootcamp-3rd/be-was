package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.view.InternalResourceView;
import webserver.view.RedirectView;
import webserver.view.ThymeleafView;
import webserver.view.View;

import java.nio.file.Paths;

public class ViewResolver {
    private static final Logger logger = LoggerFactory.getLogger(dispatcherServlet.class);

    public static View resolve(String viewName) {
        if (isRedirect(viewName)){
            return new RedirectView(viewName);
        }
        else if(isTemplate(viewName)){
            return new ThymeleafView(getAbsolutePath(viewName));
        }
        else if(isStatic(viewName)){
            return new InternalResourceView(getAbsolutePath(viewName));
        }

        return new ThymeleafView(getAbsolutePath(viewName) + ".html");
    }

    private static boolean isRedirect(String viewName) {
        return viewName.startsWith("redirect:");
    }

    public static Boolean isTemplate(String url){
        return url.endsWith(".html");
    }

    public static Boolean isStatic(String url){
        return url.matches(".*/(?:css|fonts|images|js)/.*|.*\\.(?:ico|png|jpg)");
    }

    public static String getAbsolutePath(String viewPath){
        if(isStatic(viewPath)){
            return Paths.get(System.getProperty("user.dir"), "src/main/resources/static").toString() + viewPath;

        }
        else{
            return Paths.get(System.getProperty("user.dir"), "src/main/resources/templates").toString()+"/"+viewPath;

        }
    }
}
