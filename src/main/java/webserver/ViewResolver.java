package webserver;

import java.nio.file.Paths;

public class ViewResolver {
    public static MyView resolve(String viewName) {
        if (isRedirect(viewName)){
            return new MyView(viewName);
        }
        else if(isTemplate(viewName)||isStatic(viewName)){
            return new MyView(getAbsolutePath(viewName));
        }

        return new MyView(getAbsolutePath(viewName) + ".html");
    }

    private static boolean isRedirect(String viewName) {
        return viewName.startsWith("redirect:");
    }

    public static Boolean isTemplate(String url){
        return url.endsWith(".html");
    }
    public static Boolean isStatic(String url){
        return url.startsWith("/css/")||url.startsWith("/fonts/")||url.startsWith("/images/")||url.startsWith("/js/")||url.endsWith(".ico")||url.endsWith(".png")||url.endsWith(".jpg");
    }

    private static String getAbsolutePath(String viewPath){
        if(viewPath.endsWith(".html")){
            return Paths.get(System.getProperty("user.dir"), "src/main/resources/templates").toString()+"/"+viewPath;
        }
        else{
            return Paths.get(System.getProperty("user.dir"), "src/main/resources/static").toString() + viewPath;
        }
    }
}
