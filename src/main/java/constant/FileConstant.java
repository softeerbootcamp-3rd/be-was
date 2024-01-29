package constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileConstant {
    public static final String HTML_BASE_FOLDER = "src/main/resources/templates";
    public static final String SUPPORT_FILE_BASE_FOLDER = "src/main/resources/static";
    public static final String MAIN_PAGE_PATH = "/index.html";
    public static final String LOGIN_PAGE_PATH = "/user/login.html";
    public static final String LOGIN_FAILED_PAGE_PATH = "/user/login_failed.html";

    public static final String NICKNAME_NAV = "<li class=\"nickname\">{userName}님 환영합니다.</li>\n";

    public static final String BEFORE_LOGIN_NAV = "<li class=\"active\"><a href=\"../index.html\">Posts</a></li>\n" +
                                                "<li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>\n" +
                                                "<li><a href=\"../user/form.html\" role=\"button\">회원가입</a></li>\n";

    public static final String AFTER_LOGIN_NAV = "<li class=\"active\"><a href=\"../index.html\">Posts</a></li>\n" +
                                                "<li><a href=\"/user/logout\" role=\"button\">로그아웃</a></li>\n" +
                                                "<li><a href=\"#\" role=\"button\">개인정보수정</a></li>\n";

    public static final String USER_INFO = "<tr>\n" +
                                            "<th scope=\"row\">{i}</th> <td>{userId}</td> <td>{userName}</td> <td>{userEmail}</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                                            "</tr>\n";

    public static final List<String> API_CAN_EXECUTE_WITHOUT_LOGIN = new ArrayList<>(Arrays.asList("/user/create", "/user/login"));

    public static final List<String> FILE_CAN_READ_WITHOUT_LOGIN = new ArrayList<>(Arrays.asList("/", "/index.html",
                                                                                                "/user/form.html",
                                                                                                "/user/login.html",
                                                                                                "/user/login_failed.html"));

}
