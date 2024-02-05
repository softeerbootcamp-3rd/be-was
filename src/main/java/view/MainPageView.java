package view;

import model.User;

public class MainPageView {
    private StringBuilder htmlBuilder = new StringBuilder();

    private MainPageView(User user) {
        htmlBuilder.setLength(0);
        if (user == null) {
            setHtmlBuilderInvalidUser();
        } else {
            setHtmlBuilderValidUser(user);
        }
    }

    public static MainPageView from(User user) {
        return new MainPageView(user);
    }

    public byte[] getByteArray() {
        return htmlBuilder.toString().getBytes();
    }


    //일반 화면
    private void setHtmlBuilderInvalidUser() {
        htmlBuilder.append("<!DOCTYPE html><html lang=\"kr\"><head>")
                .append("<meta http-equiv=\"content-type\" content=\"text/html charset=UTF-8\">")
                .append("<meta charset=\"utf-8\">")
                .append("<title>SLiPP Java Web Programming</title>")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">")
                .append("<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">")
                .append("<!--[if lt IE 9]><script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script><![endif]-->")
                .append("<link href=\"css/styles.css\" rel=\"stylesheet\"></head>")
                .append("<body><nav class=\"navbar navbar-fixed-top header\"><div class=\"col-md-12\">")
                .append("<div class=\"navbar-header\"><a href=\"index.html\" class=\"navbar-brand\">SLiPP</a>")
                .append("<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse1\">")
                .append("<i class=\"glyphicon glyphicon-search\"></i></button></div>")
                .append("<div class=\"collapse navbar-collapse\" id=\"navbar-collapse1\">")
                .append("<form class=\"navbar-form pull-left\"><div class=\"input-group\" style=\"max-width:470px\">")
                .append("<input type=\"text\" class=\"form-control\" placeholder=\"Search\" name=\"srch-term\" id=\"srch-term\">")
                .append("<div class=\"input-group-btn\"><button class=\"btn btn-default btn-primary\" type=\"submit\">")
                .append("<i class=\"glyphicon glyphicon-search\"></i></button></div></div></form>")
                .append("<ul class=\"nav navbar-nav navbar-right\">")
                .append("<li><a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-bell\"></i></a>")
                .append("<ul class=\"dropdown-menu\">")
                .append("<li><a href=\"https://slipp.net\" target=\"_blank\">SLiPP</a></li>")
                .append("<li><a href=\"https://facebook.com\" target=\"_blank\">Facebook</a></li></ul></li>")
                .append("<li><a href=\"./user/login.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li></ul></div></div></nav>")
                .append("<div class=\"navbar navbar-default\" id=\"subnav\"><div class=\"col-md-12\">")
                .append("<div class=\"navbar-header\">")
                .append("<a href=\"#\" style=\"margin-left:15px\" class=\"navbar-btn btn btn-default btn-plus dropdown-toggle\" data-toggle=\"dropdown\">")
                .append("<i class=\"glyphicon glyphicon-home\" style=\"color:#dd1111\"></i> Home <small><i class=\"glyphicon glyphicon-chevron-down\"></i></small></a>")
                .append("<ul class=\"nav dropdown-menu\">")
                .append("<li><a href=\"user/profile.html\"><i class=\"glyphicon glyphicon-user\" style=\"color:#1111dd\"></i> Profile</a></li>")
                .append("<li class=\"nav-divider\"></li>")
                .append("<li><a href=\"#\"><i class=\"glyphicon glyphicon-cog\" style=\"color:#dd1111\"></i> Settings</a></li></ul>")
                .append("<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse2\">")
                .append("<span class=\"sr-only\">Toggle navigation</span>")
                .append("<span class=\"icon-bar\"></span><span class=\"icon-bar\"></span><span class=\"icon-bar\"></span></button></div>")
                .append("<div class=\"collapse navbar-collapse\" id=\"navbar-collapse2\">")
                .append("<ul class=\"nav navbar-nav navbar-right\">")
                .append("<li class=\"active\"><a href=\"index.html\">Posts</a></li>")
                .append("<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>")
                .append("<li><a href=\"user/form.html\" role=\"button\">회원가입</a></li>")
                .append("<!--\n<li><a href=\"#loginModal\" role=\"button\" data-toggle=\"modal\">로그인</a></li>")
                .append("<li><a href=\"#registerModal\" role=\"button\" data-toggle=\"modal\">회원가입</a></li>\n-->")
                .append("</ul></div></div></div>")
                .append("<div class=\"container\" id=\"main\"><div class=\"col-md-12 col-sm-12 col-lg-10 col-lg-offset-1\">")
                .append("<div class=\"panel panel-default qna-list\"><ul class=\"list\"><li>")
                .append("<div class=\"wrap\"><div class=\"main\">")
                .append("<strong class=\"subject\"><a href=\"./qna/show.html\">국내에서 Ruby on Rails와 Play가 활성화되기 힘든 이유는 뭘까?</a></strong>")
                .append("<div class=\"auth-info\"><i class=\"icon-add-comment\"></i><span class=\"time\">2016-01-15 18:47</span>")
                .append("<a href=\"./user/profile.html\" class=\"author\">자바지기</a></div>")
                .append("<div class=\"reply\" title=\"댓글\"><i class=\"icon-reply\"></i><span class=\"point\">8</span></div>")
                .append("</div></div></li><li><div class=\"wrap\"><div class=\"main\">")
                .append("<strong class=\"subject\"><a href=\"./qna/show.html\">runtime 에 reflect 발동 주체 객체가 뭔지 알 방법이 있을까요?</a></strong>")
                .append("<div class=\"auth-info\"><i class=\"icon-add-comment\"></i><span class=\"time\">2016-01-05 18:47</span>")
                .append("<a href=\"./user/profile.html\" class=\"author\">김문수</a></div>")
                .append("<div class=\"reply\" title=\"댓글\"><i class=\"icon-reply\"></i><span class=\"point\">12</span></div>")
                .append("</div></div></li></ul><div class=\"row\">")
                .append("<div class=\"col-md-3\"></div><div class=\"col-md-6 text-center\">")
                .append("<ul class=\"pagination center-block\" style=\"display:inline-block\">")
                .append("<li><a href=\"#\">«</a></li>")
                .append("<li><a href=\"#\">1</a></li><li><a href=\"#\">2</a></li>")
                .append("<li><a href=\"#\">3</a></li><li><a href=\"#\">4</a></li><li><a href=\"#\">5</a></li>")
                .append("<li><a href=\"#\">»</a></li></ul></div>")
                .append("<div class=\"col-md-3 qna-write\">")
                .append("<a href=\"./qna/form.html\" class=\"btn btn-primary pull-right\" role=\"button\">질문하기</a>")
                .append("</div></div></div></div></div>")
                .append("<script src=\"js/jquery-2.2.0.min.js\"></script>")
                .append("<script src=\"js/bootstrap.min.js\"></script>")
                .append("<script src=\"js/scripts.js\"></script>")
                .append("</body></html>");
    }

    //가입한 유저명 반영한 화면
    private void setHtmlBuilderValidUser(User user) {
        htmlBuilder.append("<!DOCTYPE html><html lang=\"kr\"><head>")
                .append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">")
                .append("<meta charset=\"utf-8\">")
                .append("<title>SLiPP Java Web Programming</title>")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">")
                .append("<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">")
                .append("<!--[if lt IE 9]><script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script><![endif]-->")
                .append("<link href=\"css/styles.css\" rel=\"stylesheet\"></head>")
                .append("<body><nav class=\"navbar navbar-fixed-top header\"><div class=\"col-md-12\">")
                .append("<div class=\"navbar-header\"><a href=\"index.html\" class=\"navbar-brand\">SLiPP</a>")
                .append("<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse1\">")
                .append("<i class=\"glyphicon glyphicon-search\"></i></button></div>")
                .append("<div class=\"collapse navbar-collapse\" id=\"navbar-collapse1\">")
                .append("<form class=\"navbar-form pull-left\"><div class=\"input-group\" style=\"max-width:470px\">")
                .append("<input type=\"text\" class=\"form-control\" placeholder=\"Search\" name=\"srch-term\" id=\"srch-term\">")
                .append("<div class=\"input-group-btn\"><button class=\"btn btn-default btn-primary\" type=\"submit\">")
                .append("<i class=\"glyphicon glyphicon-search\"></i></button></div></div></form>")
                .append("<ul class=\"nav navbar-nav navbar-right\">")
                .append("<li><a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-bell\"></i></a>")
                .append("<ul class=\"dropdown-menu\">")
                .append("<li><a href=\"https://slipp.net\" target=\"_blank\">SLiPP</a></li>")
                .append("<li><a href=\"https://facebook.com\" target=\"_blank\">Facebook</a></li></ul></li>")
                .append("<li><a href=\"./user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li></ul></div></div></nav>")
                .append("<div class=\"navbar navbar-default\" id=\"subnav\"><div class=\"col-md-12\">")
                .append("<div class=\"navbar-header\">")
                .append("<a href=\"#\" style=\"margin-left:15px;\" class=\"navbar-btn btn btn-default btn-plus dropdown-toggle\" data-toggle=\"dropdown\">")
                .append("<i class=\"glyphicon glyphicon-home\" style=\"color:#dd1111;\"></i> Home <small><i class=\"glyphicon glyphicon-chevron-down\"></i></small></a>")
                .append("<ul class=\"nav dropdown-menu\">")
                .append("<li><a href=\"user/profile.html\"><i class=\"glyphicon glyphicon-user\" style=\"color:#1111dd;\"></i> Profile</a></li>")
                .append("<li class=\"nav-divider\"></li>")
                .append("<li><a href=\"#\"><i class=\"glyphicon glyphicon-cog\" style=\"color:#dd1111;\"></i> Settings</a></li></ul>")
                .append("<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse2\">")
                .append("<span class=\"sr-only\">Toggle navigation</span>")
                .append("<span class=\"icon-bar\"></span><span class=\"icon-bar\"></span><span class=\"icon-bar\"></span></button></div>")
                .append("<div class=\"collapse navbar-collapse\" id=\"navbar-collapse2\">")
                .append("<ul class=\"nav navbar-nav navbar-right\">")
                .append("<li><p class=\"navbar-text\">").append(user.getName()).append(" 님, Hi</p></li>")
                .append("<li class=\"active\"><a href=\"index.html\">Posts</a></li>")
                .append("<li><a href=\"user/logout\" role=\"button\">로그아웃</a></li>")
                .append("<li><a href=\"#\" role=\"button\">개인정보수정</a></li></ul></div></div></div>")
                .append("<div class=\"container\" id=\"main\"><div class=\"col-md-12 col-sm-12 col-lg-10 col-lg-offset-1\">")
                .append("<div class=\"panel panel-default qna-list\"><ul class=\"list\"><li>")
                .append("<div class=\"wrap\"><div class=\"main\">")
                .append("<strong class=\"subject\"><a href=\"./qna/show.html\">국내에서 Ruby on Rails와 Play가 활성화되기 힘든 이유는 뭘까?</a></strong>")
                .append("<div class=\"auth-info\"><i class=\"icon-add-comment\"></i><span class=\"time\">2016-01-15 18:47</span>")
                .append("<a href=\"./user/profile.html\" class=\"author\">자바지기</a></div>")
                .append("<div class=\"reply\" title=\"댓글\"><i class=\"icon-reply\"></i><span class=\"point\">8</span></div>")
                .append("</div></div></li><li><div class=\"wrap\"><div class=\"main\">")
                .append("<strong class=\"subject\"><a href=\"./qna/show.html\">runtime 에 reflect 발동 주체 객체가 뭔지 알 방법이 있을까요?</a></strong>")
                .append("<div class=\"auth-info\"><i class=\"icon-add-comment\"></i><span class=\"time\">2016-01-05 18:47</span>")
                .append("<a href=\"./user/profile.html\" class=\"author\">김문수</a></div>")
                .append("<div class=\"reply\" title=\"댓글\"><i class=\"icon-reply\"></i><span class=\"point\">12</span></div>")
                .append("</div></div></li></ul><div class=\"row\">")
                .append("<div class=\"col-md-3\"></div><div class=\"col-md-6 text-center\">")
                .append("<ul class=\"pagination center-block\" style=\"display:inline-block;\">")
                .append("<li><a href=\"#\">«</a></li>")
                .append("<li><a href=\"#\">1</a></li><li><a href=\"#\">2</a></li>")
                .append("<li><a href=\"#\">3</a></li><li><a href=\"#\">4</a></li><li><a href=\"#\">5</a></li>")
                .append("<li><a href=\"#\">»</a></li></ul></div>")
                .append("<div class=\"col-md-3 qna-write\">")
                .append("<a href=\"./qna/form.html\" class=\"btn btn-primary pull-right\" role=\"button\">질문하기</a>")
                .append("</div></div></div></div></div>")
                .append("<script src=\"js/jquery-2.2.0.min.js\"></script>")
                .append("<script src=\"js/bootstrap.min.js\"></script>")
                .append("<script src=\"js/scripts.js\"></script>")
                .append("</body></html>");
    }
}
