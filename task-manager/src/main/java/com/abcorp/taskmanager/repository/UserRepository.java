package com.abcorp.taskmanager.repository;

import com.abcorp.taskmanager.model.entity.User;
import com.abcorp.taskmanager.type.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailOrPhone(String email, String phone);
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("Update User u set u.status = :status where u.id = :id")
    void updateStatus(@Param(value = "id") UUID id, @Param(value = "status") UserStatus status);

    @Modifying
    @Query("Update User u set u.password = :password where u.email = :email")
    void updatePassword(@Param(value = "email") String email, @Param(value = "password") String password);
}
