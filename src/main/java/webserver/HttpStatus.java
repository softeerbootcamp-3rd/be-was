package webserver;

public enum HttpStatus {
  OK(200,"OK"),
  BAD_REQUEST(400,"BAD_REQUEST"),
  NOT_FOUND(404,"NOT_FOUND"),
  REDIRECT(302,"REDIRECT");

  final int value;
  final String message;
  HttpStatus(int i,String m) {
    value=i;
    message=m;
  }
  public int getValue(){
    return this.value;
  }
}
