package dto.session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public class Session {

    private String id;
    private String userId;          // 해당 유저의 아이디
    private LocalDateTime createTime;   // 세션 생성 시간
    private LocalDateTime lastAccessTime;     // 마지막 접속 시간 (로그인 했을 떄 갱신)
    private LocalDateTime expires;      // 세션 만료 시간 : 1시간

    public Session(String userId) {
        this.id = createSessionID();
        this.userId = userId;
        this.createTime = LocalDateTime.now();
        this.lastAccessTime = LocalDateTime.now();
        this.expires = createTime.plusHours(1);
    }

    public String getId() {
        return this.id;
    }
    public String getUserId() {
        return this.userId;
    }
    // 만료기간을 GMT 형식에 맞춰 반환
    public String getExpires() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withLocale(Locale.ENGLISH);
        return expires.format(formatter);
    }
    public String getLastAccessTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withLocale(Locale.ENGLISH);
        return lastAccessTime.format(formatter);
    }

    // 로그인 시 마지막 접속 시간 업데이트
    public void setLastAccessTime(LocalDateTime time) {
        this.lastAccessTime = time;
    }

    // 세션 ID 생성
    public String createSessionID() {
        // randomUUID 메서드를 사용하여 랜덤한 UUID 생성
        UUID uuid = UUID.randomUUID();
        // UUID를 문자열로 변환하여 세션 ID로 반환
        System.out.println("create session id: " + uuid.toString());
        return uuid.toString();
    }
}
