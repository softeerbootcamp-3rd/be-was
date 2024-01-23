package webserver;

import dto.Login;
import dto.UserDTO;

import java.util.Map;

public class HttpRequest {
    private static Long numberOfRequests = 0L;
    private final Long requestId;
    private Map<String, String> info;

    public HttpRequest(Map<String, String> messageElement){
        this.requestId = ++numberOfRequests;
        this.info = messageElement;
    }

    public String toString(){
        StringBuilder requestInfo = new StringBuilder();
        requestInfo.append("Request ID: ").append(requestId)
                .append(" [Method: ").append(info.get("Method")).append(", ")
                .append("Host: ").append(info.get("Host")).append(", ")
                .append("Path: ").append(info.get("Path")).append("]");

        return requestInfo.toString();
    }

    public String getPath(){
        return info.get("Path");
    }

    private void extractAndAddUserInfoToHeader(){
        String[] userInfo = info.get("Body").split("&");

        for (String s : userInfo) {
            String[] details = s.split("=");
            info.put(details[0], details[1]);
        }
    }

    public String getResponseMimeType(){
        String[] pathParts = getPath().split("\\.");
        return pathParts[pathParts.length-1];
    }

    public UserDTO toUserDto(){
        extractAndAddUserInfoToHeader();
        return new UserDTO(info.get("userId"), info.get("password"), info.get("name"), info.get("email"));
    }

    public String getMethod() {
        return info.get("Method");
    }

    public Login getLoginInfo(){
        String[] loginInfo = info.get("Body").split("&");
        String[] userIdPart = loginInfo[0].split("=");
        String[] passwordPart = loginInfo[1].split("=");
        String userId = userIdPart[1];
        String password = passwordPart[1];
        return new Login(userId, password);
    }
}
