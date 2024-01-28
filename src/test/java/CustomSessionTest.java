import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.session.CustomSession;

import java.util.UUID;

public class CustomSessionTest {
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  @Test
  public void register_user_success(){
    User user = new User("a","1234","abc","a@gmail.com");
    UUID sessionKey = CustomSession.registerUser(user.getUserId());
    Assertions.assertThat(user.getUserId()).isEqualTo(CustomSession.getSessionValue(sessionKey).getUserId());
  }

  @Test
  public void compare_session_value() throws InterruptedException {
    CustomSession.SessionValue sessionValue = new CustomSession.SessionValue("b");
    logger.debug("sleep for 10s......");
    Thread.sleep(10000);
    logger.debug("end sleep!!");
    CustomSession.SessionValue sessionValue2 = new CustomSession.SessionValue("b");
    Assertions.assertThat(sessionValue.equals(sessionValue2)).isTrue();
  }

  @Test
  public void register_user_failed_duplicate_session_value(){
    User user1 = new User("a","1234","abc","a@gmail.com");
    User user2 = new User("a","145","safasf","abc@gmail.com");

    CustomSession.registerUser(user1.getUserId());

    UUID result = CustomSession.registerUser(user2.getUserId());

    Assertions.assertThat(result).isNull();
  }
}
