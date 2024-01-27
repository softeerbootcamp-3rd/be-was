package webserver;

import dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

// 사용자에게 보낼 응답을 처리
public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    public static void send(DataOutputStream dos, ResponseDto responseDto) {
        try {
            // 헤더 전송
            dos.writeBytes("HTTP/1.1 " + responseDto.getStatus().toString() + " \r\n");
            for (String header : responseDto.getHeaders()) {
                dos.writeBytes(header);
            }

            dos.writeBytes("\r\n");

            // 바디가 있는 경우 바디 전송
            if (responseDto.getBody() != null) {
                dos.write(responseDto.getBody(), 0, responseDto.getBody().length);
                dos.flush();
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


}
