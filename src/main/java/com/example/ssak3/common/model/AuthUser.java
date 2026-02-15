package com.example.ssak3.common.model;

import com.example.ssak3.common.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class AuthUser implements UserDetails, OAuth2User {

    private final Long id;
    private final String email;
    private final UserRole role;

    private Map<String, Object> attributes; // OAuth2용

    // JWT 로그인용 생성자
    public AuthUser(Long id, String email, UserRole role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    // OAuth2 로그인용 생성자
    public AuthUser(Long id, String email, UserRole role,
                    Map<String, Object> attributes) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.attributes = attributes;
    }

    // ===== UserDetails =====

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return null; // JWT 기반이면 필요 없음
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // ===== OAuth2User =====

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}