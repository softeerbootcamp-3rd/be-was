package httpmessage;

import model.User;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.time.format.DateTimeFormatter;

public class HttpSession {
    private String id;
    private LocalDateTime createDate;
    private LocalDateTime expireDate;
    private static Map<String, User> values = new HashMap<>();
    public HttpSession() {}
    public HttpSession(User user){
        id = UUID.randomUUID().toString();
        values.put(id,user);
        LocalDateTime createDate = LocalDateTime.now();
        expireDate = createDate.plusMinutes(1).minusHours(9);;
    }

    public String getExpireDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zz", Locale.ENGLISH);
        return expireDate.atZone(ZoneId.of("UTC")).format(formatter);
    }

    public User getUser(String sid) {
        return values.get(sid);
    }
    public String getId() {
        return id;
    }
}
