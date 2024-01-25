package model;

import db.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserInfo {
    private HashMap<String, String> info;
    public UserInfo(HashMap<String, String> params) throws IllegalArgumentException{
        if(!validateInfo(params)) throw new IllegalArgumentException("Not valid Information !!");
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

    public boolean validateInfo(HashMap<String, String> params) {
        List<String> list = new ArrayList<>();
        list.add(params.getOrDefault("userId", ""));
        list.add(params.getOrDefault("password", ""));
        list.add(params.getOrDefault("name", ""));
        list.add(params.getOrDefault("email", ""));
        for(String info : list) {
            if(info.isEmpty()) return false;
        }
        if(Database.findUserById(params.getOrDefault("userId", "")) != null)
            return false;
        return true;
    }
}
