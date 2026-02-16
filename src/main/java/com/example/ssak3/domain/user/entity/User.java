package com.example.ssak3.domain.user.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.OAuthProvider;
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

    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDate birth;

    @Column(unique = true)
    private String phone;

    private String address;

    @Enumerated(EnumType.STRING)
    private OAuthProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;

    public User(String name, String nickname, String email, String password, LocalDate birth, String phone, String address) {

        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.birth = birth;
        this.phone = phone;
        this.address = address;
        this.role = UserRole.USER;
        this.isDeleted = false;
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }

    public void updateProvider(OAuthProvider provider) {
        this.provider = provider;
    }

    public void updatePhone() {
        this.phone = null;
    }

    public void updatePassword(String newPassword) {
        this.password = (newPassword != null) ? newPassword.replaceAll("\\s", "") : this.password;
    }

    public void update(UserUpdateRequest request) {

        this.name = (request.getName() != null) ? request.getName().replaceAll("\\s", "") : this.name;
        this.nickname = (request.getNickname() != null) ? request.getNickname().replaceAll("\\s", "") : this.nickname;
        this.birth = (request.getBirth() != null) ? request.getBirth() : this.birth;
        this.phone = (request.getPhone() != null) ? request.getPhone().replaceAll("\\s", "") : this.phone;
        this.address = (request.getAddress() != null) ? request.getAddress() : this.address;
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}
