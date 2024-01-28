package util.web;

import model.User;
import webserver.HttpRequest;

import java.util.Map;

public class SharedData {
    public static ThreadLocal<HttpRequest> request = new ThreadLocal<>();
    public static ThreadLocal<User> requestUser = new ThreadLocal<>();
    public static ThreadLocal<Map<String, String>> pathParam = new ThreadLocal<>();
}
