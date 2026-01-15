package com.example.ssak3.domain.user.model.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class UserUpdateRequest {

    @Size(min = 1, max = 30, message = "이름은 1자 이상 30자 이하여야 합니다.")
    private String name;

    @Size(min = 4, max = 30, message = "닉네임은 4자 이상 30자 이하여야 합니다.")
    private String nickname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phone;

    @Size(max = 255, message = "주소 형식이 올바르지 않습니다.")
    private String address;
}
