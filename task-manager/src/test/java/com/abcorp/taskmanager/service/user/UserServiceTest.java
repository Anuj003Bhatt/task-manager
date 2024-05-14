package com.abcorp.taskmanager.service.user;

import com.abcorp.taskmanager.exception.BadRequestException;
import com.abcorp.taskmanager.exception.NotFoundException;
import com.abcorp.taskmanager.model.entity.User;
import com.abcorp.taskmanager.model.request.AddUserDto;
import com.abcorp.taskmanager.model.response.UserDto;
import com.abcorp.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
/*
    Boolean disableUser(UUID userId);
* */
    @Test
    public void addNewUserSuccessTest(){
        UserService service = new UserServiceImpl(userRepository);
        when(userRepository.save(any())).thenReturn(new User());
        when(userRepository.findByEmailOrPhone(any(), any())).thenReturn(Optional.empty());

        service.add(new AddUserDto());
        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).findByEmailOrPhone(any(), any());
    }

    @Test
    public void addNewUserWithSamePhoneEmailTest(){
        UserService service = new UserServiceImpl(userRepository);
        when(userRepository.save(any())).thenReturn(new User());
        when(userRepository.findByEmailOrPhone(any(), any())).thenReturn(Optional.of(new User()));

        assertThrows(BadRequestException.class, () -> service.add(new AddUserDto()));
        verify(userRepository, times(0)).save(any());
        verify(userRepository, times(1)).findByEmailOrPhone(any(), any());
    }

    @Test
    public void getUsersPaginatedTest(){
        UserService service = new UserServiceImpl(userRepository);
        when(userRepository.findAll(Pageable.ofSize(2))).thenReturn(Page.empty());

        service.getUsersPaginated(Pageable.ofSize(2));
        verify(userRepository, times(1)).findAll(Pageable.ofSize(2));
    }

    @Test
    public void getUserByIdTest(){
        UserService service = new UserServiceImpl(userRepository);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));

        service.findUserById(UUID.randomUUID());

        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void getUserByIdFailTest(){
        UserService service = new UserServiceImpl(userRepository);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.findUserById(UUID.randomUUID()));
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void updateUserNullTest(){
        UserService service = new UserServiceImpl(userRepository);
        assertThrows(BadRequestException.class, () -> service.updateUser(null, null));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void updateUserTest(){
        UserService service = new UserServiceImpl(userRepository);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.updateUser(UUID.randomUUID(), null));
        verify(userRepository, times(0)).save(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void updateUserSuccessTest(){
        UserService service = new UserServiceImpl(userRepository);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(userRepository.save(any())).thenReturn(new User());
        service.updateUser(UUID.randomUUID(), new UserDto());
        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void deleteUserNullTest(){
        UserService service = new UserServiceImpl(userRepository);
        assertThrows(BadRequestException.class, () -> service.deleteUser(null));
        verify(userRepository, times(0)).delete(any());
    }

    @Test
    public void deleteUserTest(){
        UserService service = new UserServiceImpl(userRepository);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.deleteUser(UUID.randomUUID()));
        verify(userRepository, times(0)).delete(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void deleteUserSuccessTest(){
        UserService service = new UserServiceImpl(userRepository);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        service.deleteUser(UUID.randomUUID());
        verify(userRepository, times(1)).delete(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void enableUserNullTest(){
        UserService service = new UserServiceImpl(userRepository);
        assertThrows(BadRequestException.class, () -> service.enableUser(null));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void enableUserTest(){
        UserService service = new UserServiceImpl(userRepository);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.enableUser(UUID.randomUUID()));
        verify(userRepository, times(0)).save(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void enableUserSuccessTest(){
        UserService service = new UserServiceImpl(userRepository);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(userRepository.save(any())).thenReturn(new User());
        service.enableUser(UUID.randomUUID());
        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void disableUserNullTest(){
        UserService service = new UserServiceImpl(userRepository);
        assertThrows(BadRequestException.class, () -> service.disableUser(null));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void disableUserTest(){
        UserService service = new UserServiceImpl(userRepository);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.disableUser(UUID.randomUUID()));
        verify(userRepository, times(0)).save(any());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void disableUserSuccessTest(){
        UserService service = new UserServiceImpl(userRepository);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(userRepository.save(any())).thenReturn(new User());
        service.disableUser(UUID.randomUUID());
        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(any());
    }
}
