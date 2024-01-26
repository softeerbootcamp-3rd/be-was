package util;

import model.User;
import webserver.HttpRequest;

public class SharedData {
    public static ThreadLocal<HttpRequest> request = new ThreadLocal<>();
    public static ThreadLocal<User> requestUser = new ThreadLocal<>();
}