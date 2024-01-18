import db.Database;
import dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;

public class UserServiceTest {
    static final UserService userService = new UserService();
    @Test
    void 회원가입(){
        UserDTO userData = new UserDTO("golapaduck", "1111", "jiwon", "abc@naver.com");
        userService.registerUser(userData);
        Assertions.assertNotNull(Database.findUserById("golapaduck"));
    }
}
