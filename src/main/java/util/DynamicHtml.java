package util;

import data.RequestData;
import db.Database;
import db.Session;
import model.Post;
import model.User;

import java.util.*;

public class DynamicHtml {
    private DynamicHtml() {}

    public static String modifyHtml(String originalHtml, boolean isLoggedIn, RequestData requestData) {
        StringBuilder modifiedHtml = new StringBuilder(originalHtml);

        String name = "";
        if (isLoggedIn) {
            String sessionId = RequestParserUtil.parseUserRegisterQuery(requestData.getHeaderValue("Cookie")).get("sid");
            if (sessionId != null && !sessionId.isEmpty()) {
                User loggedInUser = Database.findUserById(Session.getUserIdBySessionId(sessionId));
                if (loggedInUser != null) {
                    name = loggedInUser.getName();
                }
            }
        }

        if (isLoggedIn) {
            modifyLoggedInUserContent(modifiedHtml, name);
            modifyLoggedInQnaShow(modifiedHtml, requestData);
        }

        modifyAlwaysChangingContent(modifiedHtml);

        return modifiedHtml.toString();
    }

    private static void modifyLoggedInUserContent(StringBuilder modifiedHtml, String name) {
        Map<String, String> dynamicContent = new HashMap<>();
        dynamicContent.put("<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>", ""); // `/index.html`의 로그인 버튼
        dynamicContent.put("<li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>", ""); // `/user/*.html`의 로그인 버튼
        dynamicContent.put("<li><a href=\"./user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>", "<li class=\"navbar-brand\">" + name + "님 안녕하세요</li>\r\n<li><a href=\"./user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>"); // `/index.html`의 로그인 버튼
        dynamicContent.put("<li><a href=\"../user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>", "<li class=\"navbar-brand\">" + name + "님 안녕하세요</li>\r\n<li><a href=\"../user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>"); // `/user/*.html`의 로그인 버튼

        // 사용자 리스트 동적 변경
        Collection<User> allUsers = Database.findAll();

        StringBuilder userListHtml = new StringBuilder();
        for (User user : allUsers) {
            userListHtml.append("<tr>\n")
                    .append("    <th scope=\"row\">").append(user.getUserId()).append("</th>")
                    .append("    <td>").append(user.getName()).append("</td>")
                    .append("    <td>").append(user.getEmail()).append("</td>")
                    .append("    <td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n")
                    .append("</tr>\n");
        }

        dynamicContent.put("<tr>\n" +
                "                    <th scope=\"row\">1</th> <td>javajigi</td> <td>자바지기</td> <td>javajigi@sample.net</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <th scope=\"row\">2</th> <td>slipp</td> <td>슬립</td> <td>slipp@sample.net</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                "                </tr>", userListHtml.toString());

        // 동적으로 HTML 수정
        for (Map.Entry<String, String> entry : dynamicContent.entrySet()) {
            String loggedOutText = entry.getKey();
            String loggedInText = entry.getValue();

            int index = modifiedHtml.indexOf(loggedOutText);
            if (index != -1) {
                modifiedHtml.replace(index, index + loggedOutText.length(), loggedInText);
            }
        }
    }

    private static void modifyLoggedInQnaShow(StringBuilder modifiedHtml, RequestData requestData) {
        Map<String, String> dynamicContent = new HashMap<>();

        int postId;
        if (requestData.getBody() != null) {
            int index = requestData.getBody().indexOf("postId");
            if (index != -1) {
                postId = Integer.parseInt(requestData.getBody().substring("postId=".length()));

                Post post = Database.getPostById(postId);
                assert post != null;

                // 게시글 수정사항
                dynamicContent.put("              <h2 class=\"qna-title\">InitializingBean implements afterPropertiesSet() 호출되지 않는 문제.</h2>\n",
                        "              <h2 class=\"qna-title\">" + post.getTitle() + "</h2>\n");
                dynamicContent.put("                          <a href=\"/users/92/kimmunsu\" class=\"article-author-name\">kimmunsu</a>\n",
                        "                          <a href=\"/users/92/kimmunsu\" class=\"article-author-name\">" + post.getWriter() + "</a>\n");
                dynamicContent.put("2015-12-30 01:47", post.getCreatedAt());
                dynamicContent.put("<div class=\"article-doc\">\n" +
                                "                      <p>A 에 의존성을 가지는 B라는 클래스가 있습니다.</p><p>B라는 클래스는 InitializingBean 을 상속하고 afterPropertiesSet을 구현하고 있습니다.\n" +
                                "                      서버가 가동되면서 bean들이 초기화되는 시점에 B라는 클래스의 afterPropertiesSet 메소드는</p><p>A라는 클래스의 특정 메소드인 afunc()를 호출하고 있습니다.</p>\n" +
                                "                  </div>",
                        "<div class=\"article-doc\">\n" +
                                "                      <p>" + post.getContents() + "<p>\n" + "                  </div>\n" +
                                (!post.getFileId().equals("-1") ? "<br><div class=\"article-doc\"><b>첨부파일</b><br><li><a href=\"/" + post.getFileId() + "." + post.getFileExtension() + "\" download>" + post.getFileId() + "." + post.getFileExtension() + "</a>" +
                                        getImageHtml(post.getFileId(), post.getFileExtension(), "200px", "") + "</li></div>\n" : "") +
                        "<br><br><br><br>");


                // 댓글 수정사항
                dynamicContent.put("<strong>2</strong>개의 의견", "<strong>" + post.getCommentCount() + "</strong>개의 의견");
                dynamicContent.put("<article class=\"article\" id=\"answer-1405\">\n" +
                        "                              <div class=\"article-header\">\n" +
                        "                                  <div class=\"article-header-thumb\">\n" +
                        "                                      <img src=\"https://graph.facebook.com/v2.3/1324855987/picture\" class=\"article-author-thumb\" alt=\"\">\n" +
                        "                                  </div>\n" +
                        "                                  <div class=\"article-header-text\">\n" +
                        "                                      <a href=\"/users/1/자바지기\" class=\"article-author-name\">자바지기</a>\n" +
                        "                                      <a href=\"#answer-1434\" class=\"article-header-time\" title=\"퍼머링크\">\n" +
                        "                                          2016-01-12 14:06\n" +
                        "                                      </a>\n" +
                        "                                  </div>\n" +
                        "                              </div>\n" +
                        "                              <div class=\"article-doc comment-doc\">\n" +
                        "                                  <p>이 글만으로는 원인 파악하기 힘들겠다. 소스 코드와 설정을 단순화해서 공유해 주면 같이 디버깅해줄 수도 있겠다.</p>\n" +
                        "                              </div>\n" +
                        "                              <div class=\"article-utils\">\n" +
                        "                                  <ul class=\"article-utils-list\">\n" +
                        "                                      <li>\n" +
                        "                                          <a class=\"link-modify-article\" href=\"/questions/413/answers/1405/form\">수정</a>\n" +
                        "                                      </li>\n" +
                        "                                      <li>\n" +
                        "                                          <form class=\"delete-answer-form\" action=\"/questions/413/answers/1405\" method=\"POST\">\n" +
                        "                                              <input type=\"hidden\" name=\"_method\" value=\"DELETE\">\n" +
                        "                                              <button type=\"submit\" class=\"delete-answer-button\">삭제</button>\n" +
                        "                                          </form>\n" +
                        "                                      </li>\n" +
                        "                                  </ul>\n" +
                        "                              </div>\n" +
                        "                          </article>\n" +
                        "                          <article class=\"article\" id=\"answer-1406\">\n" +
                        "                              <div class=\"article-header\">\n" +
                        "                                  <div class=\"article-header-thumb\">\n" +
                        "                                      <img src=\"https://graph.facebook.com/v2.3/1324855987/picture\" class=\"article-author-thumb\" alt=\"\">\n" +
                        "                                  </div>\n" +
                        "                                  <div class=\"article-header-text\">\n" +
                        "                                      <a href=\"/users/1/자바지기\" class=\"article-author-name\">자바지기</a>\n" +
                        "                                      <a href=\"#answer-1434\" class=\"article-header-time\" title=\"퍼머링크\">\n" +
                        "                                          2016-01-12 14:06\n" +
                        "                                      </a>\n" +
                        "                                  </div>\n" +
                        "                              </div>\n" +
                        "                              <div class=\"article-doc comment-doc\">\n" +
                        "                                  <p>이 글만으로는 원인 파악하기 힘들겠다. 소스 코드와 설정을 단순화해서 공유해 주면 같이 디버깅해줄 수도 있겠다.</p>\n" +
                        "                              </div>\n" +
                        "                              <div class=\"article-utils\">\n" +
                        "                                  <ul class=\"article-utils-list\">\n" +
                        "                                      <li>\n" +
                        "                                          <a class=\"link-modify-article\" href=\"/questions/413/answers/1405/form\">수정</a>\n" +
                        "                                      </li>\n" +
                        "                                      <li>\n" +
                        "                                          <form class=\"delete-answer-form\" action=\"/questions/413/answers/1405\" method=\"POST\">\n" +
                        "                                              <input type=\"hidden\" name=\"_method\" value=\"DELETE\">\n" +
                        "                                              <button type=\"submit\" class=\"delete-answer-button\">삭제</button>\n" +
                        "                                          </form>\n" +
                        "                                      </li>\n" +
                        "                                  </ul>\n" +
                        "                              </div>\n" +
                        "                          </article>",
                        "");
            }
        }

        // 동적으로 HTML 수정
        for (Map.Entry<String, String> entry : dynamicContent.entrySet()) {
            String loggedOutText = entry.getKey();
            String loggedInText = entry.getValue();

            int index = modifiedHtml.indexOf(loggedOutText);
            if (index != -1) {
                modifiedHtml.replace(index, index + loggedOutText.length(), loggedInText);
            }
        }
    }

    private static String getImageHtml(String fileId, String fileExtension, String maxHeight, String align) {
        // 이미지 확장자 목록
        List<String> imageExtensions = Arrays.asList("png", "jpg", "jpeg", "gif", "bmp");

        // 파일이 이미지인지 확인
        if (imageExtensions.contains(fileExtension.toLowerCase())) {
            return "<div class=\"article-doc\" style=\"max-width: 100%;\"><img src=\"/" + fileId + "." + fileExtension + "\" alt=\"Image\" style=\"max-height: "+maxHeight+"; width: auto; "+align+"\"></div>";
        }
        return ""; // 이미지가 아닌 경우 빈 문자열 반환
    }

    private static void modifyAlwaysChangingContent(StringBuilder modifiedHtml) {
        Map<String, String> dynamicContent = new HashMap<>();

        // 포스트 리스트 동적 변경
        Collection<Post> allPosts = Database.getAllPosts();
        StringBuilder postListHtml = new StringBuilder();
        postListHtml.append("<ul class=\"list\">\n");
        for (Post post : allPosts) {
            postListHtml.append("<li>\n")
                    .append("    <div class=\"wrap\">\n")
                    .append((!post.getFileId().equals("-1") ? getImageHtml(post.getFileId(), post.getFileExtension(), "50px", "float: right") : ""))
                    .append("        <div class=\"main\">\n")
                    .append("            <strong class=\"subject\">\n")
                    .append("                <a href=\"./qna/show.html?postId=" + post.getPostId() + "\">").append(post.getTitle()).append("</a>\n")
                    .append("            </strong>\n")
                    .append("            <div class=\"auth-info\">\n")
                    .append("                <i class=\"icon-add-comment\"></i>\n")
                    .append("                <span class=\"time\">").append(post.getCreatedAt()).append("</span>\n")
                    .append("                <a href=\"./user/profile.html\" class=\"author\">").append(post.getWriter()).append("</a>\n")
                    .append("            </div>\n")
                    .append("            <div class=\"reply\" title=\"댓글\">\n")
                    .append("                <i class=\"icon-reply\"></i>\n")
                    .append("                <span class=\"point\">").append(post.getPostId()).append("</span>\n")
                    .append("            </div>\n")
                    .append("        </div>\n")
                    .append("    </div>\n")
                    .append("</li>\n");
        }
        postListHtml.append("</ul>");
        dynamicContent.put("<ul class=\"list\">\n" +
                "              <li>\n" +
                "                  <div class=\"wrap\">\n" +
                "                      <div class=\"main\">\n" +
                "                          <strong class=\"subject\">\n" +
                "                              <a href=\"./qna/show.html\">국내에서 Ruby on Rails와 Play가 활성화되기 힘든 이유는 뭘까?</a>\n" +
                "                          </strong>\n" +
                "                          <div class=\"auth-info\">\n" +
                "                              <i class=\"icon-add-comment\"></i>\n" +
                "                              <span class=\"time\">2016-01-15 18:47</span>\n" +
                "                              <a href=\"./user/profile.html\" class=\"author\">자바지기</a>\n" +
                "                          </div>\n" +
                "                          <div class=\"reply\" title=\"댓글\">\n" +
                "                              <i class=\"icon-reply\"></i>\n" +
                "                              <span class=\"point\">8</span>\n" +
                "                          </div>\n" +
                "                      </div>\n" +
                "                  </div>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                  <div class=\"wrap\">\n" +
                "                      <div class=\"main\">\n" +
                "                          <strong class=\"subject\">\n" +
                "                              <a href=\"./qna/show.html\">runtime 에 reflect 발동 주체 객체가 뭔지 알 방법이 있을까요?</a>\n" +
                "                          </strong>\n" +
                "                          <div class=\"auth-info\">\n" +
                "                              <i class=\"icon-add-comment\"></i>\n" +
                "                              <span class=\"time\">2016-01-05 18:47</span>\n" +
                "                              <a href=\"./user/profile.html\" class=\"author\">김문수</a>\n" +
                "                          </div>\n" +
                "                          <div class=\"reply\" title=\"댓글\">\n" +
                "                              <i class=\"icon-reply\"></i>\n" +
                "                              <span class=\"point\">12</span>\n" +
                "                          </div>\n" +
                "                      </div>\n" +
                "                  </div>\n" +
                "              </li>\n" +
                "          </ul>", postListHtml.toString());

        // 동적으로 HTML 수정
        for (Map.Entry<String, String> entry : dynamicContent.entrySet()) {
            String staticText = entry.getKey();
            String dynamicText = entry.getValue();

            int index = modifiedHtml.indexOf(staticText);
            if (index != -1) {
                modifiedHtml.replace(index, index + staticText.length(), dynamicText);
            }
        }
    }
}
