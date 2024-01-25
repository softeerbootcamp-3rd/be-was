package service;

import factory.HttpResponseFactory;
import handler.DynamicResponseHandler;
import model.http.Status;
import model.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Map;

public class HttpResponseSendServiceImpl implements HttpResponseSendService {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponseSendService.class);
    private static final String CRLF = "\r\n";

    private static class HttpResponseServiceHolder {
        private static final HttpResponseSendService INSTANCE = new HttpResponseSendServiceImpl();
    }

    public static HttpResponseSendService getInstance() {
        return HttpResponseServiceHolder.INSTANCE;
    }

    @Override
    public void sendHttpResponse(OutputStream out, HttpResponse httpResponse) {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            setResponse(httpResponse, dos);
        } catch (IOException e) {
            logger.error("Http Response를 Send하는데 에러 발생", e);
        }
    }

    private void setResponse(HttpResponse httpResponse, DataOutputStream dos) throws IOException {
        if (httpResponse.getStatusLine().getStatus() == Status.REDIRECT) {
            setStatusAndHeaderForRedirect(dos, httpResponse);
        } else {
            setStatusAndHeader(dos, httpResponse);
            setBody(dos, httpResponse);
        }
    }

    private void setBody(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        dos.write(Base64.getDecoder().decode(httpResponse.getBody().getContent()));
        dos.flush();
    }

    private void setStatusAndHeader(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        dos.writeBytes(httpResponse.getStatusLine().getStatusHeader());
        dos.writeBytes(httpResponse.getHeaders().getContentTypeHeader());
        writeOptionHeader(dos, httpResponse);
        dos.writeBytes(httpResponse.getHeaders().getContentLengthHeader());
        dos.writeBytes(CRLF);
    }

    private void setStatusAndHeaderForRedirect(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        dos.writeBytes(httpResponse.getStatusLine().getStatusHeader());
        writeOptionHeader(dos, httpResponse);
        dos.writeBytes(CRLF);
    }


    private void writeOptionHeader(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        for (Map.Entry<String, String> entry : httpResponse.getHeaders().getOptionHeader().entrySet()) {
            String header = entry.getKey();
            String content = entry.getValue();
            dos.writeBytes(header + ": " + content + CRLF);
        }
    }
}
