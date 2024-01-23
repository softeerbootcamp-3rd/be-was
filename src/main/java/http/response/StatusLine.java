package http.response;

import java.io.DataOutputStream;
import java.io.IOException;

public class StatusLine {
    private String httpVersion;
    private HttpStatus status;

    public StatusLine(String httpVersion, HttpStatus status) {
        this.httpVersion = httpVersion;
        this.status = status;
    }

    public void writeStatusLine(DataOutputStream dos) {
        try{
            dos.writeBytes(httpVersion + " " + status.getStatusCodeAndMessage() + "\r\n");
        } catch (IOException e){

        }
    }
}
