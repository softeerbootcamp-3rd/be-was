package utils;

import model.Response;

public class HeaderBuilder {

    public static String build(Response response) {
        if (response.getCode() == 400) {
            return header400();
        }
        if (response.getCode() == 404) {
            return header404();
        }
        if (response.getCode() == 302) {
            return header302(response);
        }
        return header200(response);
    }

    private static String header400() {
        return "HTTP/1.1 400 Bad Request \r\n"
                + "\r\n";
    }

    private static String header404() {
        return "HTTP/1.1 404 Not Found \r\n"
                + "\r\n";
    }

    private static String header302(Response response) {
        return "HTTP/1.1 302 Found \n"
                + "Location: " + response.getUrl() + "\r\n"
                + "\r\n";
    }

    private static String header200(Response response) {
        return "HTTP/1.1 200 OK \n"
                + "HTTP/1.1 200 OK \r\n"
                + "Content-Type: " + ContentType.findContentType(response.getUrl())
                + ";charset=utf-8\r\n"
                + "Content-Length: " + response.getBody().length + "\r\n"
                + "\r\n";
    }
}
