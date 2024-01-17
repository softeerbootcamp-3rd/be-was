package webserver.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpStatus;
import webserver.MyHttpServletResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseBuilder {
  private final Logger logger = LoggerFactory.getLogger(HttpResponseBuilder.class);
  private final DataOutputStream dos;
  public HttpResponseBuilder(DataOutputStream outputStream){
    this.dos=outputStream;
  }
  public void flushHttpResponse(MyHttpServletResponse httpServletResponse) throws IOException {
    writeResponseHeader(httpServletResponse);
    HttpStatus httpStatus = httpServletResponse.getHttpStatus();

    if(httpStatus.equals(HttpStatus.OK)){
      writeResponseBody(httpServletResponse);
    }
    dos.flush();
  }
  private void writeResponseHeader(MyHttpServletResponse httpServletResponse) {
    HttpStatus httpStatus = httpServletResponse.getHttpStatus();
    try {
      dos.writeBytes("HTTP/1.1 "+httpStatus.getValue()+"\r\n");

      HashMap<String,String> headers = httpServletResponse.getHeaders();
      for(Map.Entry<String,String> entry : headers.entrySet()){
        dos.writeBytes(entry.getKey()+": "+entry.getValue()+"\r\n");
      }
      dos.writeBytes("\r\n");
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }
  private void writeResponseBody(MyHttpServletResponse httpServletResponse) throws IOException{
    byte[] body = httpServletResponse.getBodyBytes();
    dos.write(body, 0, body.length);
  }
}
