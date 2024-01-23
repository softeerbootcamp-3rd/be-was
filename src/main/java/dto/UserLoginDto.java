package dto;

import exception.BadRequestException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class UserLoginDto {
    private final String id;
    private final String password;

    public UserLoginDto(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public static UserLoginDto fromRequestBody(byte[] requestBody) {
        try {
            HashMap<String, String> map = new HashMap<>();
            String body = new String(requestBody);
            if (!body.isEmpty()) {
                String[] pairs = body.split("&");

                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    String key = keyValue[0];
                    String value = keyValue[1];
                    map.put(key, value);
                }
            }
            return new UserLoginDto(map.get("userId"), map.get("password"));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e){
            throw new BadRequestException("Please fill in all the necessary factors", e);
        }
    }

}
