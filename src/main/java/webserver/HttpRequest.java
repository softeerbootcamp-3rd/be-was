package webserver;

import dto.UserDTO;

import java.util.Map;

public class HttpRequest {
    private static Long numberOfRequests = 0L;
    private final Long requestId;
    private Map<String, String> header;

    public HttpRequest(Map<String, String> header){
        this.requestId = ++numberOfRequests;
        this.header = header;
    }

    public String toString(){
        StringBuilder requestInfo = new StringBuilder();
        requestInfo.append("Request ID: ").append(requestId)
                .append(" [Method: ").append(header.get("Method")).append(", ")
                .append("Host: ").append(header.get("Host")).append(", ")
                .append("Path: ").append(header.get("Path")).append("]");

        return requestInfo.toString();
    }

    public String getPath(){
        return header.get("Path");
    }

    private void extractAndAddUserInfoToHeader(){
        String[] pathParts = getPath().split("\\?");
        String[] userInfo = pathParts[1].split("&");

        for (String s : userInfo) {
            String[] details = s.split("=");
            header.put(details[0], details[1]);
        }
    }

    public String getResponseMimeType(){
        String[] pathParts = getPath().split("\\.");
        return pathParts[pathParts.length-1];
    }

    public UserDTO toUserDto(){
        extractAndAddUserInfoToHeader();
        return new UserDTO(header.get("userId"), header.get("password"), header.get("name"), header.get("email"));
    }

    public String getMethod() {
        return header.get("Method");
    }
}
