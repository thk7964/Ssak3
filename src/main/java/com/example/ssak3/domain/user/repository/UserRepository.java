package com.example.ssak3.domain.user.repository;

import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsByNickname(String nickname);

    Page<User> findAllByIsDeletedFalse(Pageable pageable);

    List<User> findAllByRole(UserRole role);
}
