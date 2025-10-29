package com.limhs.movie_project.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    public LoginDTO() {
    }

    public LoginDTO(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    @NotBlank(message = "사용자 Id값은 필수입니다")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
