package webserver.handler;

import webserver.MyHttpServletRequest;
import webserver.MyHttpServletResponse;

public interface Handler {
  MyHttpServletResponse handle(MyHttpServletRequest request);
  boolean canHandle(MyHttpServletRequest request);
}
