package webserver;

import constant.MimeType;
import constant.StaticFile;
import util.FileManager;

import java.io.IOException;

import static constant.StaticFile.*;

public class FileContentHandler {

    /**
     * <h3> 파일 컨텐츠 요청을 처리 </h3>
     * <p> html 파일 요청이면, 동적으로 처리할 부분을 처리해 응답하고 </p>
     * <p> 정적 파일 요청이라면, 파일을 bytecode 로 변환해 응답 </p>
     *
     * @param httpRequest
     * @return HttpResponse
     * @throws IOException
     */
    public static HttpResponse handle(HttpRequest httpRequest) throws IOException {

        // GET 메소드인지 확인
        if (httpRequest.getMethod().equals("GET")) {
            HttpResponse httpResponse = new HttpResponse();
            String requestPath = httpRequest.getPath();
            MimeType fileMimeType = FileManager.getMimeType(requestPath);

            // 로그인하지 않은 사용자가 로그인한 사용자만 사용할 수 있는 파일에 접근하면 로그인 페이지로 리다이렉트
            if (ThreadLocalManager.getSession() == null &&
                    fileMimeType.equals(MimeType.HTML) && !FILE_CAN_READ_WITHOUT_LOGIN.contains(requestPath)) {
                httpResponse.makeRedirect(LOGIN_PAGE_PATH);
            } else {
                // 루트 페이지로 요청이 들어온 경우에도 메인 페이지로 이동할 수 있도록
                if (requestPath.equals("/")) { httpRequest.setPath(StaticFile.MAIN_PAGE_PATH); }

                // 요청 경로에 있는 파일을 읽어 bytecode 로 받아오기
                byte[] body = FileManager.getFileBytes(requestPath);
                // 확장자를 통해 파일의 MIME 타입 가져오기
                if (fileMimeType.equals(MimeType.HTML)) {

                }
                httpResponse.makeBody(body, fileMimeType.contentType);
            }

            return httpResponse;
        }
        return null;
    }

}
