package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;

public class MyHttpServletRequest {
  private final Logger logger=LoggerFactory.getLogger(MyHttpServletRequest.class);
  private final String method;
  private final String uri;
  private final String version;
  private String Cookie;
  private String Host;
  private String Origin;
  private String Referer;
  private String Connection;
  private final HashMap<String,String> queryParameters = new HashMap<>();

  public static MyHttpServletRequest init(String requestLine){
    String[] requests = requestLine.split(" ");
    if(requests.length<3)
      return null;
    return new MyHttpServletRequest(requests[0],requests[1],requests[2]);
  }
  private String parseUri(String fullUri){
    String queryString = "";
    String uri = fullUri;

    int questionMarkIndex = fullUri.indexOf("?");
    if (questionMarkIndex != -1) {
      queryString = fullUri.substring(questionMarkIndex + 1);
      uri = fullUri.substring(0, questionMarkIndex);
    }
    if(!queryString.isEmpty())
      setQueryParameters(queryString);
    return uri;
  }
  private void setQueryParameters(String queryString){
    String[] queries = queryString.split("&");
    for(String q : queries){
      String[] queryPair = q.split("=");
      String key = queryPair[0];
      String value = queryPair[1];
      queryParameters.put(key,value);
    }
  }
  public MyHttpServletRequest(String method,String fullUri,String version){
    this.method=method;
    this.uri=parseUri(fullUri);
    this.version=version;
  }

  public void setFieldByName(String line){
    String[] strs = line.split(": ");
    String name=strs[0];
    String value=strs[1];
    try {
      Field field = this.getClass().getDeclaredField(name);
      field.setAccessible(true);
      field.set(this,value);
      field.setAccessible(false);
    }catch (NoSuchFieldException | IllegalAccessException e){
      logger.error(e.getMessage());
    }
  }

  public String toString(){
    return "\n"+method+", "+uri+", "+version+"\n"
            +Host+", "+Origin+", "+Referer+"\n"
            +Connection+"\n";
  }
  public String getUri(){
    return this.uri;
  }
  public String getQueryParameterValue(String parameterName){
    return queryParameters.get(parameterName);
  }
}
