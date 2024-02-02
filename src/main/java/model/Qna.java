package model;

import db.QnaDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Qna {

    private String id;
    private User user;

    private String title;
    private String contents;
    private String date;


    public Qna(User user, String title, String contents){
        this.id = String.valueOf(QnaDatabase.findAll().size() + 1);
        this.user = user;
        this.title = title;
        this.contents = contents;
        setDate();
    }
    public String getId(){
        return this.id;
    }

    public User getUser(){
        return this.user;
    }

    public String getTitle(){
        return this.title;
    }

    public String getContents(){
        return this.contents;
    }

    public String getDate(){
        return this.date;
    }


    public void setId(String id){
        this.id = id;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setContents(String contents){
        this.contents = contents;
    }

    private void setDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.date = LocalDateTime.now().format(formatter);
    }

}
