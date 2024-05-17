package com.abcorp.taskmanager.controller;

import com.abcorp.taskmanager.model.request.AddUserDto;
import com.abcorp.taskmanager.model.request.ChangePasswordRequest;
import com.abcorp.taskmanager.model.request.LoginRequestDto;
import com.abcorp.taskmanager.model.request.OtpVerificationRequest;
import com.abcorp.taskmanager.model.request.ResetPasswordRequest;
import com.abcorp.taskmanager.model.response.ListResponse;
import com.abcorp.taskmanager.model.response.AuthResponseDto;
import com.abcorp.taskmanager.model.response.UserDto;
import com.abcorp.taskmanager.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("users")
@Tag(name = "User")
@CrossOrigin
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Add new user",
            description = "Create a new user for the platform"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User added successfully"),
            @ApiResponse(responseCode = "400", description = "User with same email ID or phone already exists")
    })
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDto signup(@RequestBody @Valid AddUserDto addUserDto) {
        return userService.add(addUserDto);
    }

    @Operation(
            summary = "Reset Password",
            description = "Reset Password for a user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset password OTP sent"),
            @ApiResponse(responseCode = "400", description = "User with email ID not found")
    })
    @PostMapping("/reset_password")
    public Object resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        userService.resetPassword(resetPasswordRequest.getEmail());
        return Map.of("message", "Reset Password OTP has been sent to registered email ID.");
    }

    @Operation(
            summary = "OTP Verification",
            description = "Reset Password OTP verification for a user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification successful")
    })
    @PostMapping("/otp_verify")
    public Object otpVerify(@RequestBody @Valid OtpVerificationRequest otpVerificationRequest) {
        return Map.of(
                "id",
                userService.verifyOtp(otpVerificationRequest.getEmail(), otpVerificationRequest.getOtp())
        );
    }

    @Operation(
            summary = "Change Password",
            description = "Reset Password after OTP verification for a user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification successful")
    })
    @PostMapping("/change_password")
    public Object changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(
                changePasswordRequest.getEmail(),
                changePasswordRequest.getVerificationId(),
                changePasswordRequest.getNewPassword()
        );
        return Map.of("message", "Password updated successfully");
    }

    @Operation(
            summary = "User Login",
            description = "Verify user login"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Successful"),
            @ApiResponse(responseCode = "400", description = "Login Failed")
    })
    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @Operation(
            summary = "List users",
            description = "Find the list of all users"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users in response"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/page")
    public ListResponse<UserDto> listUsers(
            @PageableDefault(size = 20)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            }) Pageable pageable
    ) {
        return userService.getUsersPaginated(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Fetch user by ID",
            description = "Find a user by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user by ID"),
            @ApiResponse(responseCode = "404", description = "No user found for given ID")
    })
    public UserDto findUserById(@PathVariable("id") UUID id){
        return userService.findUserById(id);
    }

    @PatchMapping("/{id}/disable")
    @Operation(
            summary = "Disable an existing user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User disabled successfully"),
            @ApiResponse(responseCode = "404", description = "No user found for given ID")
    })
    public Map<String, String> disableUser(@PathVariable("id")UUID id){
        userService.disableUser(id);
        return Map.of("message",String.format("User %s has been disabled", id));
    }

    @PatchMapping("/{id}/enable")
    @Operation(
            summary = "Enable an already disabled user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User enabled successfully"),
            @ApiResponse(responseCode = "404", description = "No user found for given ID")
    })
    public Map<String, String> enableUser(@PathVariable("id")UUID id){
        userService.enableUser(id);
        return Map.of("message",String.format("User %s has been enabled", id));
    }
}
