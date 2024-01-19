package util.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    public static void send(DataOutputStream dos, ResponseEntity<?> responseEntity) {
        try {
            StringBuilder sb = new StringBuilder();

            HttpHeaders headers = responseEntity.getHeaders();

            int statusCode = responseEntity.getStatusCode().value();
            String reasonPhrase = HttpStatus.valueOf(statusCode).getReasonPhrase();
            sb.append("HTTP/1.1 " + statusCode + " " + reasonPhrase + " \r\n");

            Set<String> keySet = headers.keySet();
            for (String key : keySet) {
                List<String> valueList = headers.get(key);
                sb.append(key + ": ");
                for (int i = 0; i < valueList.size(); i++) {
                    sb.append(valueList.get(i));
                    if (i < valueList.size() - 1)
                        sb.append(",");
                }
                sb.append("\r\n");
            }
            sb.append("\r\n");
            dos.writeBytes(sb.toString());

            if (responseEntity.getBody() != null) {
                byte[] body = (byte[])responseEntity.getBody();
                dos.write(body, 0, body.length);
            }

            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
