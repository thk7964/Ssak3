package com.example.ssak3.domain.auth.model.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    @Size(min = 1, max = 30, message = "이름은 1자 이상 30자 이하여야 합니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력 사항입니다.")
    @Size(min = 2, max = 30, message = "닉네임은 2자 이상 30자 이하여야 합니다.")
    private String nickname;

    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|;:'\",.<>/?])[a-z\\d!@#$%^&*()_+\\-={}\\[\\]|;:'\",.<>/?]{8,16}$", message = "비밀번호는 8자 이상 16자 이하 영문 소문자, 숫자, 특수 문자를 포함해야 합니다.")
    private String password;

    @NotNull(message = "생일은 필수 입력 사항입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @NotBlank(message = "전화번호는 필수 입력 사항입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phone;

    @NotBlank(message = "주소는 필수 입력 사항입니다.")
    @Size(max = 255, message = "주소 형식이 올바르지 않습니다.")
    private String address;

}
