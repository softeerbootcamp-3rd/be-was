package model;

import java.util.HashMap;

public class UserInfo {
    private HashMap<String, String> info;
    public UserInfo(HashMap<String, String> info) {
        this.info = info;
    }
    public HashMap<String, String> getInfo() {return this.info;}
    public void setUserInfo(HashMap<String, String> info) {this.info = info;}
}
