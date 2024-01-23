package controller;

public enum HttpStatusCode {
    OK("200 OK"),
    FOUND("302 Found"),
    BAD_REQUEST("400 Bad Request"),
    NOT_FOUND("404 Not Found");

    HttpStatusCode(String s) {}
}
