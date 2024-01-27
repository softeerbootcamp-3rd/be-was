package webserver;

import constant.StaticFile;
import util.FileManager;

import java.io.IOException;

public class StaticHandler {

    /**
     * <h3> 정적 컨텐츠 요청을 처리 </h3>
     * <p> 요청이 정적 요청이고, 요청에 맞는 정적 파일이 존재하면 HttpResponse 생성 후 반환</p>
     *
     * @param httpRequest
     * @return HttpResponse
     * @throws IOException
     */
    public static HttpResponse handle(HttpRequest httpRequest) throws IOException {

        // 정적 컨텐츠 요청 - GET 메소드인지 확인
        if (httpRequest.getMethod().equals("GET")) {

            HttpResponse httpResponse = new HttpResponse();
            String basePath = StaticFile.HTML_BASE_PATH;
            String requestPath = httpRequest.getPath();
            byte[] body;

            // 루트 페이지로 요청이 들어온 경우에도 메인 페이지로 이동할 수 있도록
            if (requestPath.equals("/")) { httpRequest.setPath(StaticFile.MAIN_PAGE_PATH); }

            // 요청한 파일의 확장자가 .html 이 아닌 경우, 파일의 기본 경로를 resources/static 으로 변경
            if (!requestPath.endsWith(".html")) { basePath = StaticFile.SUPPORT_FILE_BASE_PATH; }

            // 요청 경로에 있는 파일을 읽어 bytecode 로 받아오면 성공하면
            if ((body = FileManager.getFileBytes(basePath, requestPath)) != null) {
                // 확장자를 통해 파일의 MIME 타입 가져오기
                String contentType = FileManager.getContentType(requestPath);
                httpResponse.makeBody(body, contentType);

                return httpResponse;
            }
        }
        return null;
    }

}
