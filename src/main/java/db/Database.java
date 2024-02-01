package db;



import model.Qna;
import model.User;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private static Long qnaId = 0L;

    //Maps는 생성할 때 편하게 해주는것, 기능상 차이는 없음
    private static Map<String, User> users = new ConcurrentHashMap<>();
    public static Map<Long, Qna> qnas = new ConcurrentHashMap<>();

    private static LinkedList<Qna> recentQnas = new LinkedList<>();
    public static void addQna(Qna qna){
        qnas.put(qnaId, qna);
        qna.setId(qnaId++);
        recentQnas.addFirst(qna);

        // 최신글은 100개 까지만 유지
        while(recentQnas.size() > 100)
            recentQnas.removeLast();

        updateIndexHtml();


    }
    public static LinkedList<Qna> getRecentQnas(){return recentQnas;}
    public static void addUser(User user) { users.put(user.getUserId(), user); }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static List<User> findAll() {
        return new ArrayList<> (users.values());
    }


    public static Qna findQnaById(Long id){return qnas.get(id);}

    //QNA를 생성,삭제 할 때마다 실행되는 메인화면 업데이트
    public static void updateIndexHtml(){
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n" +
                "<html lang=\"kr\">\n" +
                "\t<head>\n" +
                "\t\t<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                "\t\t<meta charset=\"utf-8\">\n" +
                "\t\t<title>SLiPP Java Web Programming</title>\n" +
                "\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n" +
                "\t\t<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                "\t\t<!--[if lt IE 9]>\n" +
                "\t\t\t<script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script>\n" +
                "\t\t<![endif]-->\n" +
                "\t\t<link href=\"css/styles.css\" rel=\"stylesheet\">\n" +
                "\t</head>\n" +
                "\t\n" +
                "\t<body>\n" +
                "<nav class=\"navbar navbar-fixed-top header\">\n" +
                " \t<div class=\"col-md-12\">\n" +
                "        <div class=\"navbar-header\">\n" +
                "\n" +
                "            <a href=\"index.html\" class=\"navbar-brand\">SLiPP</a>\n" +
                "          <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse1\">\n" +
                "          <i class=\"glyphicon glyphicon-search\"></i>\n" +
                "          </button>\n" +
                "      \n" +
                "        </div>\n" +
                "        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse1\">\n" +
                "          <form class=\"navbar-form pull-left\">\n" +
                "              <div class=\"input-group\" style=\"max-width:470px;\">\n" +
                "                <input type=\"text\" class=\"form-control\" placeholder=\"Search\" name=\"srch-term\" id=\"srch-term\">\n" +
                "                <div class=\"input-group-btn\">\n" +
                "                  <button class=\"btn btn-default btn-primary\" type=\"submit\"><i class=\"glyphicon glyphicon-search\"></i></button>\n" +
                "                </div>\n" +
                "              </div>\n" +
                "          </form>\n" +
                "          <ul class=\"nav navbar-nav navbar-right\">             \n" +
                "             <li>\n" +
                "                <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-bell\"></i></a>\n" +
                "                <ul class=\"dropdown-menu\">\n" +
                "                  <li><a href=\"https://slipp.net\" target=\"_blank\">SLiPP</a></li>\n" +
                "                  <li><a href=\"https://facebook.com\" target=\"_blank\">Facebook</a></li>\n" +
                "                </ul>\n" +
                "             </li>\n" +
                "             <li><a href=\"./user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>\n" +
                "           </ul>\n" +
                "        </div>\t\n" +
                "     </div>\t\n" +
                "</nav>\n" +
                "<div class=\"navbar navbar-default\" id=\"subnav\">\n" +
                "    <div class=\"col-md-12\">\n" +
                "        <div class=\"navbar-header\">\n" +
                "            <a href=\"#\" style=\"margin-left:15px;\" class=\"navbar-btn btn btn-default btn-plus dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-home\" style=\"color:#dd1111;\"></i> Home <small><i class=\"glyphicon glyphicon-chevron-down\"></i></small></a>\n" +
                "            <ul class=\"nav dropdown-menu\">\n" +
                "                <li><a href=\"user/profile.html\"><i class=\"glyphicon glyphicon-user\" style=\"color:#1111dd;\"></i> Profile</a></li>\n" +
                "                <li class=\"nav-divider\"></li>\n" +
                "                <li><a href=\"#\"><i class=\"glyphicon glyphicon-cog\" style=\"color:#dd1111;\"></i> Settings</a></li>\n" +
                "            </ul>\n" +
                "            \n" +
                "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse2\">\n" +
                "            \t<span class=\"sr-only\">Toggle navigation</span>\n" +
                "            \t<span class=\"icon-bar\"></span>\n" +
                "            \t<span class=\"icon-bar\"></span>\n" +
                "            \t<span class=\"icon-bar\"></span>\n" +
                "            </button>            \n" +
                "        </div>\n" +
                "        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse2\">\n" +
                "            <ul class=\"nav navbar-nav navbar-right\">\n" +
                "                <li class=\"active\"><a href=\"index.html\">Posts</a></li>\n" +
                "                <li><a href=\"user/login.html\" role=\"button\">로그인</a></li>\n" +
                "                <li><a href=\"user/form.html\" role=\"button\">회원가입</a></li>\n" +
                "                <!--\n" +
                "                <li><a href=\"#loginModal\" role=\"button\" data-toggle=\"modal\">로그인</a></li>\n" +
                "                <li><a href=\"#registerModal\" role=\"button\" data-toggle=\"modal\">회원가입</a></li>\n" +
                "                -->\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"container\" id=\"main\">\n" +
                "   <div class=\"col-md-12 col-sm-12 col-lg-10 col-lg-offset-1\">\n" +
                "      <div class=\"panel panel-default qna-list\">\n" +
                "          <ul class=\"list\">");
        LinkedList<Qna> li =  Database.getRecentQnas();
        for(int i = 0; i<Math.min(10,li.size());i++)
            sb.append(li.get(i).toThumbnail());

        sb.append("</ul>\n" +
                "          <div class=\"row\">\n" +
                "              <div class=\"col-md-3\"></div>\n" +
                "              <div class=\"col-md-6 text-center\">\n" +
                "                  <ul class=\"pagination center-block\" style=\"display:inline-block;\">\n" +
                "                      <li><a href=\"#\">«</a></li>\n" +
                "                      <li><a href=\"#\">1</a></li>\n" +
                "                      <li><a href=\"#\">2</a></li>\n" +
                "                      <li><a href=\"#\">3</a></li>\n" +
                "                      <li><a href=\"#\">4</a></li>\n" +
                "                      <li><a href=\"#\">5</a></li>\n" +
                "                      <li><a href=\"#\">»</a></li>\n" +
                "                </ul>\n" +
                "              </div>\n" +
                "              <div class=\"col-md-3 qna-write\">\n" +
                "                  <a href=\"./qna/form.html\" class=\"btn btn-primary pull-right\" role=\"button\">글쓰기</a>\n" +
                "              </div>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "<!-- script references -->\n" +
                "<script src=\"js/jquery-2.2.0.min.js\"></script>\n" +
                "<script src=\"js/bootstrap.min.js\"></script>\n" +
                "<script src=\"js/scripts.js\"></script>\n" +
                "\t</body>\n" +
                "</html>");
        String path = "/Users/user/IdeaProjects/be-was/src/main/resources/templates/index.html";
        //String path = "/Users/user/IdeaProjects/be-was/src/main/resources/static";

        try {
            Files.write(Paths.get(path), sb.toString().getBytes());
        }
        catch(Exception e){

        }

    }

}
