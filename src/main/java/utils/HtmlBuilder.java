package utils;

import constants.HtmlContent;
import db.Database;
import db.PostDatabase;
import java.util.Collection;
import model.Post;
import model.Request;
import model.User;
import webserver.Session;

public class HtmlBuilder {

    private static final String postContentHtml = "<li>"
            + "<div class=\"wrap\">"
            + "<div class=\"main\">"
            + "<strong class=\"subject\">"
            + "<a href=\"post/{{post-id}}/show.html\">{{title}}</a>"
            + "</strong>"
            + "<div class=\"auth-info\">"
            + "<i class=\"icon-add-comment\"></i>"
            + "<span class=\"time\">{{post-time}} </span>"
            + "<a href=\"./user/profile.html\" class=\"author\">{{writer}}</a>"
            + "</div>"
            + "</div>"
            + "</div>"
            + "</li>\n";

    /**
     * 페이지 내용을 동적으로 변경합니다.
     *
     * <p> 기본적으로 로그인 여부에 따라 상단의 버튼을 다르게 표시합니다.
     *
     * @param request 요청 정보
     * @param body 본문
     * @return 수정된 내용 (String)
     */
    public static String build(Request request, byte[] body) {
        String url = request.getUrl();
        String bodyString = new String(body);
        bodyString = defaultBuild(request, bodyString);

        if (url.startsWith("/user/list")) {
            bodyString = buildUserList(bodyString);
        }
        if (url.equals("/index.html")) {
            bodyString = buildPostList(bodyString);
        }
        if (url.endsWith("show.html")) {
            bodyString = buildPostContent(request, bodyString);
        }
        return bodyString;
    }

    /**
     * 글의 세부내용을 수정합니다.
     *
     * <p> url을 통해 얻은 글의 id를 조회하고 해당 글의 세부내용으로 html 파일을 수정합니다.
     *
     * @param request 요청 정보
     * @param bodyString 본문 페이지
     * @return 수정된 페이지
     */
    private static String buildPostContent(Request request, String bodyString) {
        long postId = Long.parseLong(request.getUrl().split("/")[2]);
        Post post = PostDatabase.findPostById(postId);

        return bodyString.replace("{{title}}", post.getTitle())
                .replace("{{writer}}", post.getWriter())
                .replace("{{post-time}}", post.getPostTime())
                .replace("{{contents}}", post.getContents());
    }

    /**
     * 포스팅 목록을 생성합니다.
     *
     * <p> 모든 작성글을 가져와 제목, 작성자, 작성시간, 댓글 수를 각각 알맞는 키워드에 대체하고, 본문에 추가합니다.
     *
     * @param bodyString 본문 페이지
     * @return 수정된 페이지
     */
    private static String buildPostList(String bodyString) {
        Collection<Post> allPost = PostDatabase.findAll();
        StringBuilder sb = new StringBuilder();

        for (Post post : allPost) {
            String postContent = postContentHtml.replace("{{title}}", post.getTitle())
                    .replace("{{post-id}}", String.valueOf(post.getPostId()))
                    .replace("{{writer}}", post.getWriter())
                    .replace("{{post-time}}", post.getPostTime());
            sb.append(postContent);
        }

        return bodyString.replace("{{post-list}}", sb);
    }

    /**
     * 페이지 상단의 버튼 내용을 수정합니다.
     *
     * <p> 로그인 여부를 확인하고 로그인이 되어있다면 로그아웃, 개인정보수정 버튼을 보여주며, 사용자 이름을 표시합니다.
     * 로그인이 안되어있다면 로그인, 회원가입 버튼을 보여줍니다.
     *
     * @param request    요청 정보
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
        User user = Session.getUserBySessionId(request.getHeader("Cookie"));
        String nav = navHtml.replace("{{userName}}", user.getName());
        return bodyString.replace("{{nav}}", nav);
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

        if (request.getUrl().equals("/index.html")) {
            navString = navHtml.replace("{{location-login}}", "user/login.html")
                    .replace("{{location-join}}", "user/form.html");
        } else {
            navString = navHtml.replace("{{location-login}}", "../user/login.html")
                    .replace("{{location-join}}", "../user/form.html");
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
