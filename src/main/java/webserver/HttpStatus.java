package webserver;

public enum HttpStatus {
  OK(200,"OK"),
  BAD_REQUEST(400,"BAD_REQUEST");

  int value;
  String message;
  HttpStatus(int i,String m) {
  }
}
