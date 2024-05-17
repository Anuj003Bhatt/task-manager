package com.abcorp.taskmanager.service.user;

import com.abcorp.taskmanager.model.request.AddUserDto;
import com.abcorp.taskmanager.model.request.LoginRequestDto;
import com.abcorp.taskmanager.model.response.ListResponse;
import com.abcorp.taskmanager.model.response.AuthResponseDto;
import com.abcorp.taskmanager.model.response.UserDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    AuthResponseDto add(AddUserDto addUserDto);
    ListResponse<UserDto> getUsersPaginated(Pageable pageable);
    UserDto findUserById(UUID id);
    UserDto updateUser(UUID userId, UserDto newUserDetails);
    Boolean deleteUser(UUID userId);
    Boolean enableUser(UUID userId);
    Boolean disableUser(UUID userId);
    AuthResponseDto login(LoginRequestDto loginRequestDto);
    Boolean resetPassword(String email);
    UUID verifyOtp(String email, String otp);

    void changePassword(String email, UUID verificationId, String newPassword);
}
