package webserver;

import model.RequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public static String getPath(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        String line = br.readLine();
        RequestDto requestDto = new RequestDto(line.split(" ")[0], line.split(" ")[1]);

        while ((line = br.readLine()) != null && !line.equals("")) {
            requestDto.addHeader(line.split(": ")[0], line.split(": ")[1]);
        }

        logger.debug(requestDto.getHeader());

        return requestDto.getPath();
    }
}
