package service.user;

import db.Database;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.User;
import service.Service;

public class TotalUserSearchService extends Service {

    @Override
    public byte[] execute(String method, Map<String, String> params, Map<String, String> body) {
        validate(method, params, body);
        List<User> userEntity = getTotalUserEntity();

        StringBuilder sb = new StringBuilder();
        userEntity.forEach(user -> sb.append(user.toString()).append("<br>"));

        return sb.toString().getBytes();
    }

    @Override
    public void validate(String method, Map<String, String> params, Map<String, String> body) {
        if (!method.equals("GET")){
            throw new IllegalArgumentException("method is not GET");
        }
    }

    private List<User> getTotalUserEntity() {
        return new ArrayList<>(Database.findAll());
    }

}
