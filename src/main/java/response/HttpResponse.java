package response;

import utils.HttpResponseUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private String statusLine;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;


    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }


    public void setResponse(HttpResponseStatus status, byte[] body, Map<String, String> headers) {
        this.statusLine = HttpResponseUtils.getResponseStatusLine(status);
        this.body = body;
        for (String key : headers.keySet()) {
            this.headers.put(key, headers.get(key));
        }
    }

    public String getStatusLine() {
        return statusLine;
    }

    public void setStatusLine(String statusLine) {
        this.statusLine = statusLine;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "statusLine='" + statusLine + '\'' +
                ", headers=" + headers +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
