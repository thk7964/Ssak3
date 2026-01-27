package com.example.ssak3.domain.user.repository;

import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByPhone(String phone);

    Optional<User> findByEmailAndIsDeletedFalse(String email);

    Optional<User> findByIdAndIsDeletedFalse(Long id);

    @Query("""
        SELECT u
        FROM User u
        WHERE u.isDeleted = :isDeleted AND u.role = :role
        """)
    Page<User> findUserByRole(@Param("role") UserRole role, @Param("isDeleted") boolean isDeleted, Pageable pageable);

    Optional<User> findByEmail(String email);
}
