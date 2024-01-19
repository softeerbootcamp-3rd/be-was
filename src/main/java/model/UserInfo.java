package model;

import java.util.HashMap;

public class UserInfo {
    private HashMap<String, String> info;
    public UserInfo(HashMap<String, String> info) {
        this.info = new HashMap<>();
        this.info.put("userId", info.getOrDefault("userId", ""));
        this.info.put("password", info.getOrDefault("password", ""));
        this.info.put("name", info.getOrDefault("name", ""));
        this.info.put("email", info.getOrDefault("email", ""));
    }
    public HashMap<String, String> getInfo() {return this.info;}
    public void setUserInfo(HashMap<String, String> info) {this.info = info;}
}
