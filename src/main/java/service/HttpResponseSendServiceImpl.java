package service;

import model.http.Status;
import model.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class HttpResponseSendServiceImpl implements HttpResponseSendService {
    private static class HttpResponseServiceHolder {
        private static final HttpResponseSendService INSTANCE = new HttpResponseSendServiceImpl();
    }

    public static HttpResponseSendService getInstance() {
        return HttpResponseServiceHolder.INSTANCE;
    }

    private static final Logger logger = LoggerFactory.getLogger(HttpResponseSendService.class);

    @Override
    public void sendHttpResponse(OutputStream out, HttpResponse httpResponse) {
        DataOutputStream dos = new DataOutputStream(out);
        setResponse(httpResponse, dos);
    }

    private void setResponse(HttpResponse httpResponse, DataOutputStream dos) {
        if (httpResponse.getStatusLine().getStatus() == Status.REDIRECT) {
            setStatusAndHeaderForRedirect(dos, httpResponse);
        } else {
            setStatusAndHeader(dos, httpResponse);
            setBody(dos, httpResponse);
        }
    }

    private void setBody(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.write(httpResponse.getBody().getContent(), 0, httpResponse.getBody().getContent().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void setStatusAndHeader(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.getStatusLine().getStatusHeader());
            dos.writeBytes(httpResponse.getHeaders().getContentTypeHeader());
            writeOptionHeader(dos, httpResponse);
            dos.writeBytes(httpResponse.getHeaders().getContentLengthHeader());
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void setStatusAndHeaderForRedirect(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.getStatusLine().getStatusHeader());
            writeOptionHeader(dos, httpResponse);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeOptionHeader(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        for (Map.Entry<String, String> entry : httpResponse.getHeaders().getOptionHeader().entrySet()) {
            String header = entry.getKey();
            String content = entry.getValue();
            dos.writeBytes(header + ": " + content + "\r\n");
        }
    }
}
