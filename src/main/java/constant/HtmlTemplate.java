package constant;

import util.HtmlBuilder;

import java.util.function.Function;

public enum HtmlTemplate {
    LOGIN_BTN("{{login-btn}}",
            "<li><a href=\"/user/login.html\" role=\"button\">로그인</a></li>",
            HtmlBuilder::empty, HtmlBuilder::getRaw),
    LOGIN_BTN_ACTIVE("{{login-btn-active}}",
            "<li class=\"active\"><a href=\"/user/login.html\" role=\"button\">로그인</a></li>",
            HtmlBuilder::empty, HtmlBuilder::getRaw),

    LOGOUT_BTN("{{logout-btn}}",
            "<li><a href=\"/user/logout\" role=\"button\">로그아웃</a></li>",
            HtmlBuilder::getRaw, HtmlBuilder::empty),

    SIGNUP_BTN("{{signup-btn}}",
            "<li><a href=\"/user/form.html\" role=\"button\">회원가입</a></li>",
            HtmlBuilder::empty, HtmlBuilder::getRaw),
    SIGNUP_BTN_ACTIVE("{{signup-btn-active}}",
            "<li class=\"active\"><a href=\"/user/form.html\" role=\"button\">회원가입</a></li>",
            HtmlBuilder::empty, HtmlBuilder::getRaw),

    USER_NAME("{{user-name}}",
            "<li><a id=\"userName\">{{user-name}}</a></li>",
            HtmlBuilder::replaceUser, HtmlBuilder::empty),

    USER_LIST("{{user-list}}",
            "<tr>" +
            "   <th scope=\"row\">{{order}}</th>" +
            "   <td>{{user-id}}</td>" +
            "   <td>{{user-name}}</td>" +
            "   <td>{{user-email}}</td>" +
            "   <td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>" +
        "   </tr>",
            HtmlBuilder::replaceUserList, HtmlBuilder::empty);

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
