package webserver.controller;

public class UserForm {
  private String userId;
  private String password;
  private String email;
  private String name;

  public UserForm(){
  }
  public String getUserId(){
    return this.userId;
  }
  public String getPassword(){
    return this.password;
  }
  public String getEmail(){
    return this.email;
  }
  public String getName(){
    return this.name;
  }
}
