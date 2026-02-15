package com.example.ssak3.domain.user.model.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserChangePasswordRequest {

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,16}$", message = "비밀번호는 8자 이상 16자 이하 영문 대문자, 소문자, 숫자, 특수 문자를 포함해야 합니다.")
    private String newPassword;
}
