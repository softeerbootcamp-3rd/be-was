package webserver.session;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Session {
    private final String id;
    private final String userId;
    private final Map<String, String> attributes;
    private final LocalDateTime expiredTime;

    private Session(Builder builder){
        this.id = builder.id;
        this.userId = builder.userId;
        this.attributes = builder.attributes;
        this.expiredTime = builder.expiredTime;
    }

    static Builder builder(){
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expiredTime);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("sid=").append(id);
        attributes.forEach((key, value) -> sb.append("; ").append(key).append("=").append(value));

        return sb.toString();
    }

    static class Builder{
        private String id;
        private String userId;
        private Map<String, String> attributes;
        private LocalDateTime expiredTime;

        private Builder(){
            attributes = new HashMap<>();
        }

        public Builder id(String id){
            this.id = id;

            return this;
        }

        public Builder userId(String userid){
            this.userId = userid;

            return this;
        }

        public Builder maxAge(int maxAge){
            expiredTime = LocalDateTime.now().plusSeconds(maxAge);
            attributes.put("MaxAge", String.valueOf(maxAge));

            return this;
        }

        public Session build(){
            return new Session(this);
        }
    }
}
