package webserver.session;

import webserver.MyHttpServletRequest;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class CustomSession {
  private static final HashMap<UUID,SessionValue> session = new HashMap<>();
  public static ThreadLocal<UUID> currentSession = new ThreadLocal<>();
  public static ThreadLocal<UUID> setCookie = new ThreadLocal<>();
  public static void initCurrentSession(MyHttpServletRequest request){
    String rawCookie = request.getCookie();
    if(rawCookie==null)
      return;
    String[] cookies = rawCookie.split(";");
    Optional<String> sessionKeyCookie = Arrays.stream(cookies)
            .filter((String s)->"SID".equals(findCookieKey(s))).findFirst();
    sessionKeyCookie.ifPresent(s -> currentSession.set(UUID.fromString(findCookieValue(s))));
  }
  public static void finishCurrentSession(){
    currentSession.remove();
    setCookie.remove();
  }
  public static String findCookieKey(String cookie){
    String[] cookieKeyValue = cookie.split("=");
    if(cookieKeyValue.length!=2)
      return null;
    return cookieKeyValue[0];
  }
  public static String findCookieValue(String cookie){
    String[] cookieKeyValue = cookie.split("=");
    if(cookieKeyValue.length!=2)
      return null;
    return cookieKeyValue[1];
  }
  synchronized
  public static UUID registerUser(String userId){
    UUID sessionKey = UUID.randomUUID();
    SessionValue createdSession = new SessionValue(userId);
    if(session.containsValue(createdSession))
      return null;
    CustomSession.session.put(sessionKey,createdSession);
    return sessionKey;
  }

  public static SessionValue getSessionValue(UUID sessionKey){
    return CustomSession.session.get(sessionKey);
  }

  public static class SessionValue{
    private final String userId;
    private final ZonedDateTime createdAt;
    private ZonedDateTime lastAccess;

    public String getUserId(){
      return this.userId;
    }
    public SessionValue(String userId){
      this.userId=userId;
      this.createdAt=ZonedDateTime.now();
      this.lastAccess=this.createdAt;
    }

    @Override
    public boolean equals(Object obj) {
      if(obj.getClass().equals(SessionValue.class))
        return this.userId.equals(((SessionValue) obj).getUserId());
      return false;
    }
  }
}
