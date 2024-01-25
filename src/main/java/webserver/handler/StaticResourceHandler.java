package webserver.handler;

import webserver.MyHttpServletRequest;
import webserver.MyHttpServletResponse;

import java.util.ArrayList;
import java.util.List;

public class StaticResourceHandler implements Handler{
  private final List<String> staticExtensions = new ArrayList<>();
  public StaticResourceHandler(){
    staticExtensions.add("js");
    staticExtensions.add("css");
    staticExtensions.add("image");
    staticExtensions.add("woff");
    staticExtensions.add("ttf");
  }
  @Override
  public MyHttpServletResponse handle(MyHttpServletRequest request) {
    return MyHttpServletResponse.staticResource(request.getUri());
  }

  @Override
  public boolean canHandle(MyHttpServletRequest request) {
    for(String extension : staticExtensions){
      if(request.getAccept().contains(extension) || request.getUri().contains(extension))
        return true;
    }
    return false;
  }
}
