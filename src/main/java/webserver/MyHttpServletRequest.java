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
  private String accept;
  private String cookie;
  private String host;
  private String origin;
  private String referer;
  private String connection;
  private String contentLength;
  private final HashMap<String,String> queryParameters = new HashMap<>();
  private final HashMap<String,String> body = new HashMap<>();

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
    insertToHashMap(queryString,this.queryParameters);
  }
  public void setBody(String bodyString){
    insertToHashMap(bodyString,this.body);
  }
  public MyHttpServletRequest(String method,String fullUri,String version){
    this.method=method;
    this.uri=parseUri(fullUri);
    this.version=version;
  }

  /**
   * http 정보 파싱 후 Reflection API 사용해서 특정 field에 주입
   */
  public void setFieldByName(String line){
    String[] strs = line.split(": ");
    String name=toCamelCase(strs[0]);
    String value=strs[1];
    //key이름과 일치하는 필드가 있을 때에는 field.set을 통해 주입, 없을 때에는 null로 남겨두기.
    try {
      Field field = this.getClass().getDeclaredField(name);
      field.setAccessible(true);
      field.set(this,value);
      field.setAccessible(false);
    }catch (NoSuchFieldException | IllegalAccessException e){
      //MyHttpServletRequest에 헤더의 key 이름과 똑같은 이름을 가진 필드가 없을 때는 NoSuchFieldException 발생
      logger.error(e.getMessage());
    }
  }

  public String toString(){
    return "\n"+method+", "+uri+", "+version+"\n"
            +host+", "+origin+", "+referer+"\n"
            +connection+", "+contentLength+"\n";
  }
  public String getUri(){
    return this.uri;
  }
  public String getMethod(){
    return this.method;
  }
  public String getAccept(){return  this.accept;}
  public String getQueryParameterValue(String parameterName){
    return queryParameters.get(parameterName);
  }
  public String getBodyValue(String bodyName){ return body.get(bodyName);}
  public String getCookie(){
    return this.cookie;
  }

  private String toCamelCase(String rawString){
    String[] parts = rawString.split("-");
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < parts.length; i++) {
      if (i == 0) {
        result.append(parts[i].toLowerCase());
      } else {
        result.append(parts[i].substring(0, 1).toUpperCase());
        result.append(parts[i].substring(1).toLowerCase());
      }
    }
    return result.toString();
  }

  public boolean canReadBody(){
    return contentLength!=null;
  }
  public int getContentLength(){
    return Integer.parseInt(this.contentLength);
  }

  public void insertToHashMap(String paramString,HashMap<String,String> map){
    String[] queries = paramString.split("&");
    for(String q : queries){
      String[] queryPair = q.split("=");
      String key = queryPair[0];
      if(queryPair.length<2){
        map.put(key,null);
        continue;
      }
      String value = queryPair[1];
      map.put(key,value);
    }
  }
}
