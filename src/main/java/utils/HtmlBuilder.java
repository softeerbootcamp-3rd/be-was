package utils;

import constants.HtmlContent;
import db.Database;
import java.util.Collection;
import model.Request;
import model.User;
import webserver.Session;

public class HtmlBuilder {

    /**
     * 동적인 HTML 주소인지 확인 후 내용을 수정합니다.
     *
     * @param request 요청 정보
     * @param body    : 본문
     * @return 수정된 내용 (String)
     */
    public static String build(Request request, byte[] body) {
        String url = request.getUrl();
        String bodyString = new String(body);
        bodyString = defaultBuild(request, bodyString);

        if (url.startsWith("/user/list")) {
            bodyString = buildUserList(bodyString);
        }
        return bodyString;
    }

    /**
     * 페이지 상단의 버튼 내용을 수정합니다.
     *
     * @param request 요청 정보
     * @param bodyString 본문 페이지
     * @return 수정된 페이지
     */
    private static String defaultBuild(Request request, String bodyString) {
        String cookie = request.getHeader("Cookie");

        if (cookie == null || Session.getUserBySessionId(cookie) == null) {
            // status: logout
            String nav = getNavString(request);
            return bodyString.replace("{{nav}}", nav);
        }
        // status : login
        String navHtml = HtmlContent.NAV_LOGIN.getText();
        return bodyString.replace("{{nav}}", navHtml);
    }

    /**
     * 로그인 버튼과 회원가입 버튼의 링크를 수정합니다.
     *
     * @param request 요청 정보
     * @return 수정된 버튼 html
     */
    private static String getNavString(Request request) {
        String navHtml = HtmlContent.NAV_LOGOUT.getText();
        String navString;

        if (request.getUrl().startsWith("/user")) {
            navString = navHtml.replace("{{location-login}}", "../user/login.html")
                    .replace("{{location-join}}", "../user/form.html");
        } else {
            navString = navHtml.replace("{{location-login}}", "user/login.html")
                    .replace("{{location-join}}", "user/form.html");
        }

        return navString;
    }

    /**
     * 사용자 목록 내용을 수정합니다.
     *
     * <p> 모든 사용자 목록을 조회한 후 사용자마다 내용을 생성해서 추가합니다.
     *
     * @param bodyString 본문
     * @return 수정된 본문 (String)
     */
    private static String buildUserList(String bodyString) {
        Collection<User> users = Database.findAll();
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            count++;
            String userContentHtml = HtmlContent.USER_CONTENT.getText();
            String userContent = userContentHtml.replace("{{count}}", Integer.toString(count))
                    .replace("{{userId}}", user.getUserId())
                    .replace("{{userName}}", user.getName())
                    .replace("{{userEmail}}", user.getEmail());
            sb.append(userContent);
        }
        return bodyString.replace("{{userList}}", sb);
    }
}
