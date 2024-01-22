package model;

import java.util.HashMap;

public class UserInfo {
    private HashMap<String, String> info;
    public UserInfo(HashMap<String, String> params) {
        this.info = new HashMap<>();
        this.info.put("userId", params.getOrDefault("userId", ""));
        this.info.put("password", params.getOrDefault("password", ""));
        this.info.put("name", params.getOrDefault("name", ""));
        this.info.put("email", params.getOrDefault("email", ""));
    }
    public HashMap<String, String> getInfo() {return this.info;}
    public void addInfo(String key, String value) {
        this.info.put(key, value);
    }
    public void deleteInfo(String key) {
        info.remove(key);
    }
}
