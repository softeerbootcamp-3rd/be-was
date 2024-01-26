package constant;

import model.User;
import util.HtmlBuilder;

import java.util.function.BiFunction;

public enum HtmlTemplate {
    LOGIN_BTN("{{login-btn}}",
            "<li><a href=\"/user/login.html\" role=\"button\">로그인</a></li>",
            HtmlBuilder::empty, HtmlBuilder::replaceOne),
    LOGIN_BTN_ACTIVE("{{login-btn-active}}",
            "<li class=\"active\"><a href=\"/user/login.html\" role=\"button\">로그인</a></li>",
            HtmlBuilder::empty, HtmlBuilder::replaceOne),

    LOGOUT_BTN("{{logout-btn}}",
            "<li><a href=\"/user/logout\" role=\"button\">로그아웃</a></li>",
            HtmlBuilder::replaceOne, HtmlBuilder::empty),

    SIGNUP_BTN("{{signup-btn}}",
            "<li><a href=\"/user/form.html\" role=\"button\">회원가입</a></li>",
            HtmlBuilder::empty, HtmlBuilder::replaceOne),
    SIGNUP_BTN_ACTIVE("{{signup-btn-active}}",
            "<li class=\"active\"><a href=\"/user/form.html\" role=\"button\">회원가입</a></li>",
            HtmlBuilder::empty, HtmlBuilder::replaceOne),

    USER_NAME("{{user-name}}",
            "<li><a id=\"userName\">{{user-name}}</a></li>",
            HtmlBuilder::replaceOne, HtmlBuilder::empty),

    USER_LIST("{{user-list}}",
            "<tr>" +
            "   <th scope=\"row\">{{order}}</th>" +
            "   <td>{{user-id}}</td>" +
            "   <td>{{user-name}}</td>" +
            "   <td>{{user-email}}</td>" +
            "   <td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>" +
        "   </tr>",
            HtmlBuilder::replaceList, HtmlBuilder::empty);

    private final String originalValue;
    private final String template;
    private final BiFunction<String, User, String> loggedInFunction;
    private final BiFunction<String, User, String> loggedOutFunction;

    HtmlTemplate(String originalValue, String template,
                 BiFunction<String, User, String> loggedInFunction, BiFunction<String, User, String> loggedOutFunction) {
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

    public BiFunction<String, User, String> getLoggedInFunction() {
        return  this.loggedInFunction;
    }

    public BiFunction<String, User, String> getLoggedOutFunction() {
        return  this.loggedOutFunction;
    }
}
