package service;

import model.User;
import request.SignUpRequest;
import util.SingletonUtil;


public class UserService {
    public void signUp(SignUpRequest signUpRequest) {
        User user = new User(signUpRequest.getUserId(), signUpRequest.getPassword(), signUpRequest.getName(), signUpRequest.getEmail());
        SingletonUtil.getDatabase().addUser(user);
    }
}
