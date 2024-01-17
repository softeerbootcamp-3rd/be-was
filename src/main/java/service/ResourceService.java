package service;
public class ResourceService {


    public String getPath(String file_extension, String path) {
        if (file_extension.equals("html")) {
            return "./src/main/resources/templates" + path;
        }
        else if ("css".equals(file_extension) || "jpg".equals(file_extension) || "png".equals(file_extension) ||
                "eot".equals(file_extension) || "svg".equals(file_extension) || "ttf".equals(file_extension) ||
                "woff".equals(file_extension) || "woff2".equals(file_extension) || "ico".equals(file_extension) ||
                "js".equals(file_extension)) {
            return "./src/main/resources/static" + path;
        }
        else{
            return "404";
        }

    }

    public String getContextType(String file_extension){

        //html 파일 처리
        if (file_extension.equals("html")) {
            return "text/html;charset=utf-8";
        }
        // CSS 파일 처리
        else if (file_extension.equals("css")) {
            return "text/css;charset=utf-8";
        }
        // CSS 파일 처리
        else if (file_extension.equals("js")) {
            return "application/javascript;charset=utf-8";
        }
        else if (file_extension.equals("png")) {
            return "image/png";
        }
        else if (file_extension.equals("jpg")) {
            return "image/jpg";
        }
        else if (file_extension.equals("ico")) {
            return "image/x-icon";
        }
        else if (file_extension.equals("eot")) {
            return "font/eot";
        }
        else if (file_extension.equals("svg")) {
            return "font/svg";
        }
        else if (file_extension.equals("ttf")) {
            return "font/ttf";
        }
        else if (file_extension.equals("woff")) {
            return "font/woff";
        }
        else if (file_extension.equals("woff2")) {
            return "font/woff2";
        }
        // 다른 파일 형식 처리도 추가 가능
        else {
            return "error";
        }
    }
}
