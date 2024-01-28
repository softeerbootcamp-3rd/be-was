package constant;

import util.html.HtmlBuilder;
import util.html.QnaHtml;
import util.html.UserHtml;

import java.util.function.Function;

public enum HtmlTemplate {
    LOGIN_BTN("<!--login-btn-->",
            "<li><a href=\"/user/login.html\" role=\"button\">로그인</a></li>",
            HtmlBuilder::empty, HtmlBuilder::getRaw),
    LOGIN_BTN_ACTIVE("<!--login-btn-active-->",
            "<li class=\"active\"><a href=\"/user/login.html\" role=\"button\">로그인</a></li>",
            HtmlBuilder::empty, HtmlBuilder::getRaw),

    LOGOUT_BTN("<!--logout-btn-->",
            "<li><a href=\"/user/logout\" role=\"button\">로그아웃</a></li>",
            HtmlBuilder::getRaw, HtmlBuilder::empty),

    SIGNUP_BTN("<!--signup-btn-->",
            "<li><a href=\"/user/form.html\" role=\"button\">회원가입</a></li>",
            HtmlBuilder::empty, HtmlBuilder::getRaw),
    SIGNUP_BTN_ACTIVE("<!--signup-btn-active-->",
            "<li class=\"active\"><a href=\"/user/form.html\" role=\"button\">회원가입</a></li>",
            HtmlBuilder::empty, HtmlBuilder::getRaw),

    USER_NAME("<!--user-name-->",
            "<li><a id=\"userName\"><!--user-name--></a></li>",
            UserHtml::replaceUser, HtmlBuilder::empty),

    USER_LIST("<!--user-list-->",
            "<tr>" +
            "   <th scope=\"row\"><!--order--></th>" +
            "   <td><!--user-id--></td>" +
            "   <td><!--user-name--></td>" +
            "   <td><!--user-email--></td>" +
            "   <td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>" +
        "   </tr>",
            UserHtml::replaceUserList, HtmlBuilder::empty),

    QNA_LIST("<!--qna-list-->",
            "<li>\n" +
            "                  <div class=\"wrap\">\n" +
            "                      <div class=\"main\">\n" +
            "                          <strong class=\"subject\">\n" +
            "                              <a href=\"./qna/show.html?qnaId=<!--qna-id-->\"><!--title--></a>\n" +
            "                          </strong>\n" +
            "                          <div class=\"auth-info\">\n" +
            "                              <i class=\"icon-add-comment\"></i>\n" +
            "                              <span class=\"time\"><!--create-date--></span>\n" +
            "                              <a href=\"./user/profile.html\" class=\"author\"><!--user-name--></a>\n" +
            "                          </div>\n" +
            "                          <div class=\"reply\" title=\"댓글\">\n" +
            "                              <i class=\"icon-reply\"></i>\n" +
            "                              <span class=\"point\"><!--comments--></span>\n" +
            "                          </div>\n" +
            "                      </div>\n" +
            "                  </div>\n" +
            "              </li>",
            QnaHtml::replaceQnaList, QnaHtml::replaceQnaList),

    QNA_PAGINATION("<!--qna-pagination-->",
            "<li class=\"<!--active-->\"><a href=\"/index.html?page=<!--link-->\"><!--page-number--></a></li>",
            QnaHtml::replacePagination, QnaHtml::replacePagination),

    QNA_TITLE("<!--qna-->",
            "<header class=\"qna-header\">\n" +
                    "              <h2 class=\"qna-title\"><!--title--></h2>\n" +
                    "          </header>\n" +
                    "          <div class=\"content-main\">\n" +
                    "              <article class=\"article\">\n" +
                    "                  <div class=\"article-header\">\n" +
                    "                      <div class=\"article-header-thumb\">\n" +
                    "                          <img src=\"https://graph.facebook.com/v2.3/100000059371774/picture\" class=\"article-author-thumb\" alt=\"\">\n" +
                    "                      </div>\n" +
                    "                      <div class=\"article-header-text\">\n" +
                    "                          <a href=\"/users/92/kimmunsu\" class=\"article-author-name\"><!--writer--></a>\n" +
                    "                          <a href=\"/questions/413\" class=\"article-header-time\" title=\"퍼머링크\">\n" +
                    "                              <!--create-date-->\n" +
                    "                              <i class=\"icon-link\"></i>\n" +
                    "                          </a>\n" +
                    "                      </div>\n" +
                    "                  </div>\n" +
                    "                  <div class=\"article-doc\">\n" +
                    "                      <!--contents-->\n" +
                    "                  </div>\n" +
                    "                  <div class=\"article-utils\">\n" +
                    "                      <ul class=\"article-utils-list\">\n" +
                    "                          <li>\n" +
                    "                              <a class=\"link-modify-article\" href=\"/questions/<!--qna-id-->/form\">수정</a>\n" +
                    "                          </li>\n" +
                    "                          <li>\n" +
                    "                              <form class=\"form-delete\" action=\"/questions/423\" method=\"POST\">\n" +
                    "                                  <input type=\"hidden\" name=\"_method\" value=\"DELETE\">\n" +
                    "                                  <button class=\"link-delete-article\" type=\"submit\">삭제</button>\n" +
                    "                              </form>\n" +
                    "                          </li>\n" +
                    "                          <li>\n" +
                    "                              <a class=\"link-modify-article\" href=\"/index.html\">목록</a>\n" +
                    "                          </li>\n" +
                    "                      </ul>\n" +
                    "                  </div>\n" +
                    "              </article>\n" +
                    "\n" +
                    "              <!--comments-->\n" +
                    "          </div>",
            QnaHtml::replaceQna, QnaHtml::replaceQna);
    ;

    private final String originalValue;
    private final String template;
    private final Function<String, String> loggedInFunction;
    private final Function<String, String> loggedOutFunction;

    HtmlTemplate(String originalValue, String template,
                 Function<String, String> loggedInFunction, Function<String, String> loggedOutFunction) {
        this.originalValue = originalValue;
        this.template = template;
        this.loggedInFunction = loggedInFunction;
        this.loggedOutFunction = loggedOutFunction;
    }

    public String getOriginalValue() {
        return this.originalValue;
    }

    public String getTemplate() {
        return this.template;
    }

    public Function<String, String> getLoggedInFunction() {
        return  this.loggedInFunction;
    }

    public Function<String, String> getLoggedOutFunction() {
        return  this.loggedOutFunction;
    }
}
