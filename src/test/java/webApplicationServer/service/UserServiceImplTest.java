package webApplicationServer.service;

import db.Database;
import dto.UserSignUpDto;
import exception.BadRequestException;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;
import service.UserServiceImpl;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceImplTest {
    private UserService userService;

    @BeforeEach
    void setUP(){
        this.userService = new UserServiceImpl();
        Database.clear();
    }
    @Test
    @DisplayName("중복되는 사용자가 없으면 회원가입이 완료된다.")
    void signUp() {
        //given
        UserSignUpDto userSignUpDto = new UserSignUpDto("테스트", "1234", "이준현", "dlwnsgus07@naver.com");
        //when
        userService.signUp(userSignUpDto);
        //then
        Optional<User> userOptional = Database.findUserById("테스트");
        assertThat(userOptional.isPresent()).isTrue();
        User user =  userOptional.get();
        assertThat(user.getUserId()).isEqualTo(userSignUpDto.getId());
        assertThat(user.getPassword()).isEqualTo(userSignUpDto.getPassword());
        assertThat(user.getName()).isEqualTo(userSignUpDto.getName());
        assertThat(user.getEmail()).isEqualTo(userSignUpDto.getEmail());
    }

    @Test
    @DisplayName("중복되는 사용자가 있다면 BadRequestException을 Throw한다.")
    void duplicateSignUp(){
        //given
        Database.addUser(new User("테스트", "1234", "이준현", "dlwnsgus07@naver.com"));
        UserSignUpDto userSignUpDto = new UserSignUpDto("테스트", "1234", "이준현", "dlwnsgus07@naver.com");
        //when & then
        assertThrows(BadRequestException.class, ()->{
            userService.signUp(userSignUpDto);
        },"ID already exists");
    }

    @Test
    @DisplayName("User정보에 빈칸이 들어올 시 BadRequestException을 Throw한다.")
    void userSignUpValidation(){
        assertThrows(BadRequestException.class, ()->{
            userService.signUp(new UserSignUpDto("", "1234", "이준현", "dlwnsgus07@naver.com"));
        }, "ID cannot be empty");
        assertThrows(BadRequestException.class, ()->{
            userService.signUp(new UserSignUpDto("테스트", "", "이준현", "dlwnsgus07@naver.com"));
        }, "Password cannot be empty");
        assertThrows(BadRequestException.class, ()->{
            userService.signUp(new UserSignUpDto("테스트", "1234", "", "dlwnsgus07@naver.com"));
        }, "Name cannot be empty");
        assertThrows(BadRequestException.class, ()->{
            userService.signUp(new UserSignUpDto("테스트", "1234", "이준현", ""));
        }, "Invalid email format");
    }
    @Test
    @DisplayName("email정보에 잘못된 형식으로 들어올 시 BadRequestException을 Throw한다.")
    void emailValidation(){
        assertThrows(BadRequestException.class, ()->{
            userService.signUp(new UserSignUpDto("테스트", "1234", "이준현", "dlwnsgus07"));
        }, "Invalid email format");
    }

    @Test
    @DisplayName("UserService가 Signleton으로 클래스를 동일하게 반환한다.")
    void singleton(){
        UserService userService1 = UserServiceImpl.getInstance();
        UserService userService2 = UserServiceImpl.getInstance();
        assertThat(userService1).isSameAs(userService2);
    }
}
