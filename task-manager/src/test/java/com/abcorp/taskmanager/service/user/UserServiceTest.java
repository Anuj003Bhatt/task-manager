package com.abcorp.taskmanager.service.user;

import com.abcorp.taskmanager.config.MailConfig;
import com.abcorp.taskmanager.exception.BadRequestException;
import com.abcorp.taskmanager.exception.NotFoundException;
import com.abcorp.taskmanager.model.entity.User;
import com.abcorp.taskmanager.model.request.AddUserDto;
import com.abcorp.taskmanager.model.request.LoginRequestDto;
import com.abcorp.taskmanager.model.response.AuthResponseDto;
import com.abcorp.taskmanager.model.response.UserDto;
import com.abcorp.taskmanager.repository.UserRepository;
import com.abcorp.taskmanager.service.crypto.unidirectional.UnidirectionalCryptoService;
import com.abcorp.taskmanager.service.crypto.unidirectional.UnidirectionalCryptoServiceImpl;
import com.abcorp.taskmanager.service.mail.MailService;
import com.abcorp.taskmanager.type.UserStatus;
import com.abcorp.taskmanager.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MailService mailService;

    @MockBean
    private MailConfig mailConfig;

    @MockBean
    private JavaMailSender mailSender;

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    public UserService createService() {
        JwtUtil jwtUtil = new JwtUtil();
        jwtUtil.setSecret("test");
        jwtUtil.setExpiration(1000000L);
        UnidirectionalCryptoService service = new UnidirectionalCryptoServiceImpl(PASSWORD_ENCODER);
        return new UserServiceImpl(userRepository, service, jwtUtil, mailService);
    }
    @Test
    public void addNewUserSuccessTest(){
        UserService service = createService();
        when(userRepository.save(any())).thenReturn(User.builder().id(UUID.randomUUID()).build());
        when(userRepository.findByEmailOrPhone(any(), any())).thenReturn(Optional.empty());

        service.add(new AddUserDto());
        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).findByEmailOrPhone(any(), any());
    }

    @Test
    public void addNewUserWithSamePhoneEmailTest(){
        UserService service = createService();
        when(userRepository.save(any())).thenReturn(new User());
        when(userRepository.findByEmailOrPhone(any(), any())).thenReturn(Optional.of(new User()));

        assertThrows(BadRequestException.class, () -> service.add(new AddUserDto()));
        verify(userRepository, times(0)).save(any());
        verify(userRepository, times(1)).findByEmailOrPhone(any(), any());
    }

    @Test
    public void getUsersPaginatedTest(){
        UserService service = createService();
        when(userRepository.findAll(Pageable.ofSize(2))).thenReturn(Page.empty());

        service.getUsersPaginated(Pageable.ofSize(2));
        verify(userRepository, times(1)).findAll(Pageable.ofSize(2));
    }

    @Test
    public void getUserByIdTest(){
        UserService service = createService();
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        service.findUserById(UUID.randomUUID());

        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void getUserByIdFailTest(){
        UserService service = createService();
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.findUserById(UUID.randomUUID()));
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void updateUserNullTest(){
        UserService service = createService();
        assertThrows(BadRequestException.class, () -> service.updateUser(null, null));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void updateUserTest(){
        UserService service = createService();
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.updateUser(UUID.randomUUID(), null));
        verify(userRepository, times(0)).save(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void updateUserSuccessTest(){
        UserService service = createService();
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(userRepository.save(any())).thenReturn(new User());
        service.updateUser(UUID.randomUUID(), new UserDto());
        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void deleteUserNullTest(){
        UserService service = createService();
        assertThrows(BadRequestException.class, () -> service.deleteUser(null));
        verify(userRepository, times(0)).delete(any());
    }

    @Test
    public void deleteUserTest(){
        UserService service = createService();
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.deleteUser(UUID.randomUUID()));
        verify(userRepository, times(0)).delete(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void deleteUserSuccessTest(){
        UserService service = createService();
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        service.deleteUser(UUID.randomUUID());
        verify(userRepository, times(1)).delete(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void enableUserNullTest(){
        UserService service = createService();
        assertThrows(BadRequestException.class, () -> service.enableUser(null));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void enableUserTest(){
        UserService service = createService();
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.enableUser(UUID.randomUUID()));
        verify(userRepository, times(0)).save(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void enableUserSuccessTest(){
        UserService service = createService();
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        doNothing().when(userRepository).updateStatus(any(),any());
        service.enableUser(UUID.randomUUID());
        verify(userRepository, times(1)).updateStatus(any(), any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void disableUserNullTest(){
        UserService service = createService();
        assertThrows(BadRequestException.class, () -> service.disableUser(null));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void disableUserTest(){
        UserService service = createService();
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.disableUser(UUID.randomUUID()));
        verify(userRepository, times(0)).save(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void disableUserSuccessTest(){
        UserService service = createService();
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        doNothing().when(userRepository).updateStatus(any(), any());
        service.disableUser(UUID.randomUUID());
        verify(userRepository, times(1)).updateStatus(any(), any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void loginSuccessTest(){
        String original = "test@abc#1";
        UserService service = createService();

        String encrypted = PASSWORD_ENCODER.encode(original);
        User user = User.builder().status(UserStatus.ACTIVE).id(UUID.randomUUID()).email("test@email.com").password(encrypted).build();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        AuthResponseDto authResponseDto = service.login(new LoginRequestDto(user.getEmail(),original));
        verify(userRepository, times(1)).findByEmail(any());
    }

    @Test
    public void loginFailTest(){
        String original = "test@abc#1";
        UserService service = createService();

        String encrypted = PASSWORD_ENCODER.encode(original);
        User user = User.builder().status(UserStatus.ACTIVE).email("test@email.com").password(encrypted).build();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        assertThrows(
                BadRequestException.class,
                () -> service.login(new LoginRequestDto(user.getEmail(),"test"))
        );
        verify(userRepository, times(1)).findByEmail(any());
    }

    @Test
    public void loginDisabledUserTest(){
        String original = "test@abc#1";
        UserService service = createService();

        String encrypted = PASSWORD_ENCODER.encode(original);
        User user = User.builder().status(UserStatus.INACTIVE).email("test@email.com").password(encrypted).build();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        assertThrows(
                BadRequestException.class,
                () -> service.login(new LoginRequestDto(user.getEmail(),original))
        );
        verify(userRepository, times(1)).findByEmail(any());
    }

    @Test
    public void loginUserNotFound(){
        UserService service = createService();

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(
                NotFoundException.class,
                () -> service.login(new LoginRequestDto("test","test"))
        );
        verify(userRepository, times(1)).findByEmail(any());
    }

    @Test
    public void resetPasswordFail(){
        UserService service = createService();

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(
                NotFoundException.class,
                () -> service.resetPassword("abc@xyz.com")
        );
        verify(userRepository, times(1)).findByEmail(any());
    }

    @Test
    public void resetPasswordSuccess(){
        UserService service = createService();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        doNothing().when(mailService).sendOtp(any(), any());

        service.resetPassword("abc@xyz.com");
        verify(userRepository, times(1)).findByEmail(any());
        verify(mailService, times(1)).sendOtp(any(), any());
    }

    @Test
    public void resetPasswordVerificationFail(){
        UserService service = createService();
        assertThrows(
                BadRequestException.class,
                () -> service.verifyOtp("abc@xyz.com", "otp")
        );
    }
}
