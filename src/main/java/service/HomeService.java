package service;

import model.User;


public class HomeService {
    public static String getUserProfile(User user){
        return getUserProfileToString(user.getName(), user.getEmail());
    }
    private static String getUserProfileToString(String name, String email) {
        StringBuilder sb = new StringBuilder();

        sb.append("<div class=\"media-body\">\n")
                .append("<h4 class=\"media-heading\">").append(name).append("</h4>")
                .append("<p>\n")
                .append("<a href=\"#\" class=\"btn btn-xs btn-default\"><span class=\"glyphicon glyphicon-envelope\"></span>&nbsp;")
                .append(email).append("</a>")
                .append("</p>\n")
                .append("</div>");

        return sb.toString();
    }
}
