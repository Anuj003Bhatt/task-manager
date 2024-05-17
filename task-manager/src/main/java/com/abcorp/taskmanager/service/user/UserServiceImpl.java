package com.abcorp.taskmanager.service.user;

import com.abcorp.taskmanager.exception.BadRequestException;
import com.abcorp.taskmanager.exception.NotFoundException;
import com.abcorp.taskmanager.model.entity.User;
import com.abcorp.taskmanager.model.request.AddUserDto;
import com.abcorp.taskmanager.model.request.LoginRequestDto;
import com.abcorp.taskmanager.model.response.ListResponse;
import com.abcorp.taskmanager.model.response.AuthResponseDto;
import com.abcorp.taskmanager.model.response.UserDto;
import com.abcorp.taskmanager.repository.UserRepository;
import com.abcorp.taskmanager.service.crypto.unidirectional.UnidirectionalCryptoService;
import com.abcorp.taskmanager.service.mail.MailService;
import com.abcorp.taskmanager.type.UserStatus;
import com.abcorp.taskmanager.util.BridgeUtil;
import com.abcorp.taskmanager.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private Map<String, String> OTPs = new HashMap<>();
    private Map<String, String> verificationIDs = new HashMap<>();
    private final UserRepository userRepository;
    private final UnidirectionalCryptoService unidirectionalCryptoService;
    private final JwtUtil jwtUtil;
    private final MailService mailService;

    @Override
    public AuthResponseDto add(AddUserDto addUserDto) {
        Optional<User> userOp = userRepository.findByEmailOrPhone(addUserDto.getEmail(), addUserDto.getPhone());
        if(userOp.isPresent()){
            log.error("User with same email ID or phone already exists");
            throw new BadRequestException("User with same email ID or phone already exists");
        }
        User user = User
                .builder()
                .name(addUserDto.getName())
                .email(addUserDto.getEmail())
                .password(addUserDto.getPassword())
                .phone(addUserDto.getPhone())
                .status(UserStatus.ACTIVE)
                .build();
        UserDto userDto = userRepository.save(user).toDto();
        return AuthResponseDto
                .builder()
                .user(userDto)
                .authToken(jwtUtil.generateToken(userDto))
                .build();
    }

    @Override
    public ListResponse<UserDto> getUsersPaginated(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return BridgeUtil.buildPaginatedResponse(page);
    }

    @Override
    public UserDto findUserById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException(String.format("No user found for id %s", id))
                )
                .toDto();
    }

    @Override
    public UserDto updateUser(UUID userId, UserDto newUserDetails) {
        if (userId == null) {
            log.error("user ID passed in for update was blank");
            throw new BadRequestException("User ID for update cannot be blank");
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    log.error("No user found for ID {}", userId);
                    return new NotFoundException("No user found for ID %s", userId);
                }
        );
        user.setName(newUserDetails.getName());
        user.setEmail(newUserDetails.getEmail());
        user.setPhone(newUserDetails.getPhone());
        user.setStatus(newUserDetails.getStatus());
        user = userRepository.save(user);
        return user.toDto();
    }

    @Override
    @Transactional
    public Boolean deleteUser(UUID userId) {
        if (userId == null) {
            log.error("user ID passed in for update was blank");
            throw new BadRequestException("User ID for update cannot be blank");
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    log.error("No user found for ID {}", userId);
                    return new NotFoundException("No user found for ID %s", userId);
                }
        );
        userRepository.delete(user);
        return true;
    }

    @Override
    @Transactional
    public Boolean enableUser(UUID userId) {
        if (userId == null) {
            log.error("user ID passed in for update was blank");
            throw new BadRequestException("User ID for update cannot be blank");
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    log.error("No user found for ID {}", userId);
                    return new NotFoundException("No user found for ID %s", userId);
                }
        );
        userRepository.updateStatus(user.getId(), UserStatus.ACTIVE);
        return true;
    }

    @Override
    @Transactional
    public Boolean disableUser(UUID userId) {
        if (userId == null) {
            log.error("user ID passed in for update was blank");
            throw new BadRequestException("User ID for update cannot be blank");
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    log.error("No user found for ID {}", userId);
                    return new NotFoundException("No user found for ID %s", userId);
                }
        );
        userRepository.updateStatus(user.getId(), UserStatus.INACTIVE);
        return true;
    }

    @Override
    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(
                () -> new NotFoundException("Email ID '%s' does not exist in the system", loginRequestDto.getEmail())
        );
        if (!user.getStatus().getIsActive()) {
            throw new BadRequestException("User has been disabled");
        }
        Boolean verificationStatus = unidirectionalCryptoService.verify(user.getPassword(), loginRequestDto.getPassword());
        if (verificationStatus) {
            return AuthResponseDto
                    .builder()
                    .user(user.toDto())
                    .authToken(jwtUtil.generateToken(user.toDto()))
                    .build();
        }
        throw new BadRequestException("Invalid email ID/password");
    }

    @Override
    public Boolean resetPassword(String email) {
        userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("No user found with this email.")
        );
        String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
        this.OTPs.put(email, otp);
        mailService.sendOtp(email, otp);
        return true;
    }

    @Override
    public UUID verifyOtp(String email, String otp) {
        if (this.OTPs.containsKey(email) && this.OTPs.get(email).equalsIgnoreCase(otp)){
            UUID id = UUID.randomUUID();
            verificationIDs.put(id.toString(), email);
            return id;
        }
        throw new BadRequestException("OTP Verification failed.");
    }

    @Override
    @Transactional
    public void changePassword(String email, UUID verificationId, String newPassword) {
        if (this.verificationIDs.containsKey(verificationId.toString())
                && this.verificationIDs.get(verificationId.toString()).equalsIgnoreCase(email)) {
            userRepository.findByEmail(email).orElseThrow(
                    () -> new NotFoundException("No user found with this email.")
            );
            userRepository.updatePassword(email, newPassword);
            return;
        }
        throw new BadRequestException("Invalid Password Change request");

    }
}
