package service;

import model.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseServiceImpl implements HttpResponseService {
    private volatile static HttpResponseService httpResponseService = null;

    public static HttpResponseService getInstance(){
        if (httpResponseService == null) {
            synchronized (HttpResponseService.class) {
                if (httpResponseService == null) {
                    httpResponseService = new HttpResponseServiceImpl();
                }
            }
        }
        return httpResponseService;
    }
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseService.class);
    @Override
    public void sendHttpResponse(OutputStream out, HttpResponse httpResponse){
        DataOutputStream dos = new DataOutputStream(out);
        setStatusAndHeader(dos, httpResponse);
        setBody(dos, httpResponse);
    }

    private void setBody(DataOutputStream dos, HttpResponse httpResponse){
        try {
            dos.write(httpResponse.getBody().getContent(), 0, httpResponse.getBody().getContent().length);
            dos.flush();
        } catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    private void setStatusAndHeader(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.getStatusLine().getStatusHeader());
            dos.writeBytes(httpResponse.getHeaders().getContentTypeHeader());
            dos.writeBytes(httpResponse.getHeaders().getContentLengthHeader());
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}