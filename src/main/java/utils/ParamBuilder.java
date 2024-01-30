package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import model.Request;

public class ParamBuilder {

    /**
     * 요청 url에서 쿼리의 키와 값을 분리해 반환합니다.
     *
     * @param url 요청 url
     * @return 파라미터 (Map)
     */
    public static Map<String, String> getParamFromUrl(String url) {
        String[] splitQuery = url.split("\\?")[1].split("&");
        return getParamMap(splitQuery);
    }

    /**
     * 요청 메시지의 본문에서 쿼리의 키와 값을 분리해 반환합니다.
     *
     * @param body 요청 메시지 본문
     * @return 파라미터 (Map)
     */
    public static Map<String, String> getParamFromBody(String body) {
        String[] splitQuery = body.split("&");
        return getParamMap(splitQuery);
    }

    /**
     * 분리된 쿼리문을 키와 값의 자료구조로 만듭니다.
     *
     * <p> 퍼센트 인코딩되는 "@" 문자를 그대로 저장합니다.
     *
     * @param splitQuery 분리된 쿼리문
     * @return 파라미터 (Map)
     */
    private static Map<String, String> getParamMap(String[] splitQuery) {
        Map<String, String> params = new HashMap<>();

        for (String query : splitQuery) {
            String[] split = query.split("=", -1);
            if (split[1].contains("%40")) {
                split[1] = split[1].replace("%40", "@");
            }
            params.put(split[0], split[1]);
        }

        return params;
    }

    public static Map<String, String> getParamFromMultipart(byte[] body, Request request) {
        Map<String, String> params = new HashMap<>();
        String header = request.getHeader("Content-Type");
        String boundary = header.split("boundary=")[1];

        try {
            String bodyString = new String(body, "ISO-8859-1");
            String[] parts = bodyString.split("--" + boundary);

            for (int i = 1; i < parts.length - 1; i++) {
                String part = parts[i].trim();

                String[] headersAndBody = part.split("\r\n\r\n", 2);
                if (headersAndBody.length == 1) continue;
                String headers = headersAndBody[0];
                String bodyPart = headersAndBody[1];

                String[] disposition = headers.split("Content-Disposition:")[1].trim().split(";");
                String paramName = disposition[1].split("=")[1].replaceAll("\"", "");

                if (headers.contains("Content-Type: image")) {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageName = timeStamp + ".png";
                    String imagePath = "src/main/resources/images/" + imageName;
                    try (FileOutputStream fos = new FileOutputStream(imagePath)) {
                        fos.write(bodyPart.getBytes("ISO-8859-1"));
                    } catch (IOException e) {
                        throw new IOException(e);
                    }
                    params.put(paramName + "_image", imageName);
                } else {
                    params.put(paramName, bodyPart);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return params;
    }
}
