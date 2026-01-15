package com.example.ssak3.domain.user.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.domain.user.model.request.UserUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    // 일반 유저 생성자
    public User(String name, String nickname, String email, String password, LocalDate birth, String phone, String address) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.birth = birth;
        this.phone = phone;
        this.address = address;
        this.role = UserRole.USER;
    }

    // 관리자 생성자
    public User(String name, String nickname, String email, String password, LocalDate birth, String phone, String address, UserRole role) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.birth = birth;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public void update(UserUpdateRequest request) {
        this.name = (request.getName() != null && !request.getName().contains(" ")) ? request.getName() : this.name;
        this.nickname = (request.getNickname() != null && !request.getNickname().contains(" ")) ? request.getNickname() : this.nickname;
        this.birth = (request.getBirth() != null) ? request.getBirth() : this.birth;
        this.phone = (request.getPhone() != null && !request.getPhone().contains(" ")) ? request.getPhone() : this.phone;
        this.address = (request.getAddress() != null && !request.getAddress().contains(" ")) ? request.getAddress() : this.address;
    }
}
