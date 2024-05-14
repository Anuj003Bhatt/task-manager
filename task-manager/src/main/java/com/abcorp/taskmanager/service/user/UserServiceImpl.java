package com.abcorp.taskmanager.service.user;

import com.abcorp.taskmanager.exception.BadRequestException;
import com.abcorp.taskmanager.exception.NotFoundException;
import com.abcorp.taskmanager.model.entity.User;
import com.abcorp.taskmanager.model.request.AddUserDto;
import com.abcorp.taskmanager.model.response.ListResponse;
import com.abcorp.taskmanager.model.response.SignupResponseDto;
import com.abcorp.taskmanager.model.response.UserDto;
import com.abcorp.taskmanager.repository.UserRepository;
import com.abcorp.taskmanager.type.UserStatus;
import com.abcorp.taskmanager.util.BridgeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public SignupResponseDto add(AddUserDto addUserDto) {
        Optional<User> userOp = userRepository.findByEmailOrPhone(addUserDto.getEmail(), addUserDto.getPhone());
        if(userOp.isPresent()){
            log.error("User with same email ID or phone already exists");
            throw new BadRequestException("User with same email ID or phone already exists");
        }
        User user = User
                .builder()
                .name(addUserDto.getName())
                .email(addUserDto.getEmail())
                .phone(addUserDto.getPhone())
                .status(UserStatus.ACTIVE)
                .build();
        UserDto userDto = userRepository.save(user).toDto();
        return SignupResponseDto
                .builder()
                .user(userDto)
                .authToken("test")
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
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        return true;
    }

    @Override
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
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        return true;
    }
}
