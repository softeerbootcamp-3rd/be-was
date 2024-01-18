package webserver;

public class ViewResolver {
    public static MyView resolve(String viewName) {
        if(isTemplate(viewName)||isStatic(viewName)){
            return new MyView(viewName);
        }

        return new MyView(viewName + ".html");
    }

    public static Boolean isTemplate(String url){
        return url.endsWith(".html");
    }
    public static Boolean isStatic(String url){
        return url.startsWith("/css/")||url.startsWith("/fonts/")||url.startsWith("/images/")||url.startsWith("/js/")||url.endsWith(".ico")||url.endsWith(".png")||url.endsWith(".jpg");
    }
}
