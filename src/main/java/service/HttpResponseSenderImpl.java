package service;

import model.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseSenderImpl implements HttpResponseSender {
    private static class HttpResponseServiceHolder{
        private static final HttpResponseSender INSTANCE = new HttpResponseSenderImpl();
    }

    public static HttpResponseSender getInstance(){
        return HttpResponseServiceHolder.INSTANCE;
    }
    private static final Logger logger = LoggerFactory.getLogger(HttpResponseSender.class);
    @Override
    public void sendHttpResponse(OutputStream out, HttpResponse httpResponse){
        DataOutputStream dos = new DataOutputStream(out);
        if (httpResponse.getHeaders().getLocation() == null) {
            setStatusAndHeader(dos, httpResponse);
            setBody(dos, httpResponse);
        }
        else{
            setStatusAndHeaderForRedirect(dos, httpResponse);
        }
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
    private void setStatusAndHeaderForRedirect(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.getStatusLine().getStatusHeader());
            dos.writeBytes(httpResponse.getHeaders().getLocationTypeHeader());
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}