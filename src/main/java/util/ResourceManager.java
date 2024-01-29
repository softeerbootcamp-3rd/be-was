package util;

import dao.UserDao;
import db.Database;
import model.User;
import type.MimeType;
import webserver.http.HttpRequest;
import webserver.http.HttpStatus;
import webserver.http.HttpHeader;
import webserver.http.ResponseEntity;

import java.io.*;
import java.util.*;

public class ResourceManager {

    private static final String DEFAULT_PATH = "src/main/resources";
    private static final String LOGOUT_BUTTON = "logoutButton";
    private static final String LOGIN_BUTTON = "loginButton";
    private static final String SIGNUP_BUTTON = "singUpButton";

    public static ResponseEntity handleStaticResource(HttpRequest request) throws IOException {
        String path = request.getPath().equals("/") ? "/index.html" : request.getPath();
        MimeType mimeType = MimeType.getMimeTypeByExtension(StringParser.extractFileExtension(path));

        StringBuilder pathBuilder = new StringBuilder(DEFAULT_PATH);
        if (mimeType.equals(MimeType.HTML)) {
            pathBuilder.append("/templates");
        } else {
            pathBuilder.append("/static");
        }
        pathBuilder.append(path);

        File file = new File(pathBuilder.toString());
        if (!file.exists() || !file.isFile())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Map<String, List<String>> header = new HashMap<>();
        header.put(HttpHeader.CONTENT_TYPE, Collections.singletonList(mimeType.getContentType()));

        // 동적 HTML : 로그인 상태
        if (mimeType.equals(MimeType.HTML) && request.getCookie() != null) {
            String SID = StringParser.getCookieValue(request.getCookie(), "SID");
            if (SessionManager.isValidateSession(SID)) {
                String html = new String(readAllBytes(file));
                String userId = (String) SessionManager.getAttribute(SID, "user");
                // 사용자 이름 표시
                html = changeHTMLButtonStatus(html, SIGNUP_BUTTON, true);
                html = changeHTMLButtonStatus(html, LOGIN_BUTTON, true);
                html = changeHTMLIncludeUserName(html, UserDao.findUserNameByUserId(userId));

                if (path.equals("/user/list.html")) {
                    html = changeHTMLGetUserList(html);
                }

                header.put(HttpHeader.CONTENT_LENGTH, Collections.singletonList(String.valueOf(html.getBytes().length)));
                return new ResponseEntity<>(HttpStatus.OK, header, html);
            }
        } else if (mimeType.equals(MimeType.HTML) && request.getCookie() == null) {
            String html = new String(readAllBytes(file));
            // 로그아웃 버튼 숨김
            html = changeHTMLButtonStatus(html, LOGOUT_BUTTON, true);

            header.put(HttpHeader.CONTENT_LENGTH, Collections.singletonList(String.valueOf(html.getBytes().length)));
            return new ResponseEntity<>(HttpStatus.OK, header, html);
        }

        header.put(HttpHeader.CONTENT_LENGTH, Collections.singletonList(String.valueOf(file.length())));
        return new ResponseEntity<>(HttpStatus.OK, header, file);
    }

    public static byte[] readAllBytes(File file) throws IOException {
        byte[] byteArray = null;

        try (FileInputStream fis = new FileInputStream(file)) {
            byteArray = new byte[(int) file.length()];
            fis.read(byteArray);
        }

        return byteArray;
    }

    public static String changeHTMLIncludeUserName(String original, String userName) {
        String changed = "<a id=\"userNameButton\" style=\"display: block;\">" + userName + " 님" + "</a>";
        return original.replace("<a id=\"userNameButton\" style=\"display: none;\"></a>",
                changed);
    }

    public static String changeHTMLButtonStatus(String original, String buttonId, boolean hide) {
        String originalStyle = "none";
        String changedStyle = "block";
        if (hide) {
            originalStyle = "block";
            changedStyle = "none";
        }
        String target = "id=\"" + buttonId + "\" style=\"display: " + originalStyle + ";\"";
        String changed = "id=\"" + buttonId + "\" style=\"display: " + changedStyle + ";\"";
        return original.replace(target, changed);
    }

    public static String changeHTMLGetUserList(String original) {
        StringBuilder listBuilder = new StringBuilder("<tbody>").append(System.lineSeparator());
        int id = 1;
        for (User user : UserDao.findAll()) {
            listBuilder.append("<tr>").append(System.lineSeparator());
            listBuilder.append("<th scope=\"row\">").append(id).append("</th> ");
            listBuilder.append("<td>").append(user.getUserId()).append("</td> ");
            listBuilder.append("<td>").append(user.getName()).append("</td> ");
            listBuilder.append("<td>").append(user.getEmail()).append("</td>");
            listBuilder.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>").append(System.lineSeparator());
            listBuilder.append("</tr>").append(System.lineSeparator());

            id++;
        }
        listBuilder.append("</tbody>");
        return original.replace("<tbody>" + System.lineSeparator() + "              </tbody>",
                listBuilder.toString());
    }


}

