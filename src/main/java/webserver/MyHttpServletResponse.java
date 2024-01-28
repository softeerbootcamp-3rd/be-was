package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.session.CustomSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

public class MyHttpServletResponse {
  private static final Logger logger = LoggerFactory.getLogger(MyHttpServletResponse.class);
  private byte[] body;
  private final HashMap<String,String> headers = new HashMap<>();
  private final HttpStatus httpStatus;

  //Controller로부터 적절한 응답을 받았을 때 호출하는 생성자
  //redirect 여부를 우선적으로 구분 후 redirect여부에 따라 헤더값 & body값 다르게 설정
  //예외가 발생했을 때 Http 예외 응답 정보를 담을 수 있도록 추가 생성자 작성 예정
  public MyHttpServletResponse(String controllerReturnValue){
    headers.put("Content-Type","text/html;charset=UTF-8");
    int redirectIndex=controllerReturnValue.indexOf("redirect:");
    //redirect 응답일땐 redirectIndex는 0
    if(redirectIndex==0){
      this.httpStatus=HttpStatus.REDIRECT;
      headers.put("Location",controllerReturnValue.replace("redirect:",""));
      return;
    }
    //redirect 응답이 아닐 땐 200 응답
    this.httpStatus=HttpStatus.OK;
    setBody(controllerReturnValue);
  }
  public MyHttpServletResponse(){
    httpStatus=HttpStatus.BAD_REQUEST;
  }
  public static MyHttpServletResponse staticResource(String resourcePath){
    byte[] body;
    try (InputStream in = MyHttpServletResponse.class.getResourceAsStream("/static"+resourcePath)){
      if(in==null)
        throw new RuntimeException();
      body = in.readAllBytes();
    }catch (IOException ioException){
      throw new RuntimeException();
    }
    return new MyHttpServletResponse(HttpStatus.OK,body);
  }
  public MyHttpServletResponse(HttpStatus httpStatus,byte[] body){
    this.httpStatus=httpStatus;
    this.body=body;
  }
  public HttpStatus getHttpStatus(){
    return this.httpStatus;
  }
  public byte[] getBodyBytes() {
    return body;
  }
  public HashMap<String,String> getHeaders(){
    return headers;
  }
  //resourcePath가 주어졌을 때, 해당 경로의 resource를 읽어오는 메서드.
  // 추후 다른 형식의 데이터를 읽어오도록 오버라이딩해서 확장 가능
  public void setBody(String resourcePath){
    try (InputStream in = this.getClass().getResourceAsStream(resourcePath)){
      if(in==null)
        throw new RuntimeException();
      body = in.readAllBytes();
    }catch (IOException ioException){
      logger.error(ioException.getMessage());
    }
  }
  public void addCookieHeader(){
    UUID cookie = CustomSession.setCookie.get();
    if(cookie!=null)
      headers.put("Set-Cookie","sid="+cookie+"; PATH=/");
  }
}
