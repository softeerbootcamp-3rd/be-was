package Functions;

import db.Database;
import db.QnaDatabase;
import model.Qna;
import model.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileBytes {

    private String fileURL;


    public static byte[] FilesreadAllBytes(String fileURL, User user, boolean isHtml){

        byte[] body = new byte[0];

        if(isHtml) {
            StringBuilder content = new StringBuilder();


            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileURL), StandardCharsets.UTF_8))) {
                String line;
                boolean imbed = false;
                int currentUser = 0;
                int post = 0;
                while ((line = reader.readLine()) != null) {

                    if(imbed && line.contains("<li><a href=\"user/login.html\" role=\"button\">")){
                        continue;
                    }

                    if(user != null && line.contains("<h4 class=\"media-heading\">")){
                        content.append("<h4 class=\"media-heading\">").append(user.getName()).append("</h4>");
                        continue;
                    }
                    if(user != null && line.contains("<a href=\"#\" class=\"btn btn-xs btn-default\"><span class=\"glyphicon glyphicon-envelope\"></span>&nbsp;")){
                        content.append("<a href=\"#\" class=\"btn btn-xs btn-default\"><span class=\"glyphicon glyphicon-envelope\"></span>&nbsp;").append(user.getEmail()).append("</a>");
                        continue;
                    }


                    content.append(line).append(System.lineSeparator());
                    System.out.println("line:  " + line);



                    if (user != null && line.contains("<ul class=\"nav navbar-nav navbar-right\">") && !imbed) {
                        content.append("<li>");
                        content.append("<div class=\"name\">" + user.getName() + " 님 " +  "</div>");
                        content.append("</li>");
                        imbed = true;
                    }


                    ////////// user list
                    if(user != null && line.contains("</tr>")){
                        List<User> userList =  Database.findAll();

                        currentUser++;
                        System.out.println(currentUser);
                        if (currentUser == 3) {

                            for(User signedUser : userList) {
                                content.append("<tr>");
                                content.append("<th scope=\"row\">" + currentUser++ + "</th>");
                                content.append("<td>" +signedUser.getUserId() + "</td>");
                                content.append("<td>" +signedUser.getName() + "</td>");
                                content.append("<td>" +signedUser.getEmail() + "</td>");
                                content.append("<td>");
                                content.append("<a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a>");
                                content.append("</td>");
                                content.append("</tr>");
                            }
                        }

                    }


                    if(line.contains("<ul class=\"list\">")) { // index.html 게시글
                        List<Qna> qnaLists = QnaDatabase.findAll();
                        for(Qna qna : qnaLists){
                            content.append("<li>");
                            content.append("<div class=\"wrap\">");
                            content.append("<div class=\"main\">");
                            content.append("<strong class=\"subject\">");
                            content.append("<a href=\"./qna/show.html\">").append(qna.getTitle()).append("</a>");
                            content.append("</strong>");
                            content.append("<div class=\"auth-info\">");
                            content.append("<i class=\"icon-add-comment\"></i>");
                            content.append("<span class=\"time\">").append(qna.getDate()).append("</span>");
                            content.append("<a href=\"./user/profile.html\" class=\"author\">").append(qna.getUser().getName()).append("</a>");
                            content.append("</div>");
                            content.append("<div class=\"reply\" title=\"댓글\">");
                            content.append("<i class=\"icon-reply\"></i>");
                            content.append("<span class=\"point\">").append(qna.getId()).append("</span>");
                            content.append("</div>");
                            content.append("</div>");
                            content.append("</div>");
                            content.append("</li>");

                        }

                    }


                }
                return content.toString().getBytes(StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else{

            try (FileInputStream fis = new FileInputStream(fileURL);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                return body = bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        return body;
    }


}
