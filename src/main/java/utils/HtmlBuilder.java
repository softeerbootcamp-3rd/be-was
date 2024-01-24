package utils;

import constants.Html;
import db.Database;
import java.util.Collection;
import model.User;

public class HtmlBuilder {

    /**
     * 동적인 HTML 주소인지 확인 후 내용을 수정합니다.
     *
     * @param url 요청 타겟
     * @param body : 본문
     * @return 수정된 내용 (String)
     */
    public static String build(String url, byte[] body) {
        String bodyString = new String(body);
        if (url.startsWith("/user/list")) {
            bodyString = buildUserList(bodyString);
        }
        return bodyString;
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
            String userContentHtml = Html.USER_CONTENT.getText();
            String userContent = userContentHtml.replace("{{count}}", Integer.toString(count))
                    .replace("{{userId}}", user.getUserId())
                    .replace("{{userName}}", user.getName())
                    .replace("{{userEmail}}", user.getEmail());
            sb.append(userContent);
        }
        return bodyString.replace("{{userList}}",sb);
    }
}
