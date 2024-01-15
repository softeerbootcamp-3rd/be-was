package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ParsingUtil {

    private final static Logger logger = LoggerFactory.getLogger(ParsingUtil.class);

    //Request Header에서 url을 읽어옴
    public static ArrayList<String> getUrls(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        String line = br.readLine();
        logger.debug("request line : {}", line);

        ArrayList<String> url = new ArrayList<>();
        url.add(line.split(" ")[1]);

        //url 읽어오기
        while(!line.equals("")){
            line = br.readLine();

            if (line != null && line.equals("GET")) {
                String[] tokens = line.split(" ");

                url.add(tokens[1]);
            }

            //header 정보 출력
            logger.debug("header : {}", line);
        }
        return url;
    }
}
