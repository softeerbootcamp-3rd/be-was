package webserver;

import model.User;

public class CreateUserService {

    User user;

    public CreateUserService(String path){
        String temp = path.split("\\?")[1];
        String[] temp2 = temp.split("&");

        String[] imformation = new String[4];

        for(int i=0; i< temp2.length; i++){
            imformation[i] = temp2[i].split("=")[1];
        }
        this.user = new User(imformation[0],imformation[1],imformation[2],imformation[3]);
    }

}
