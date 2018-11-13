package ru.kpfu.itis.androidlab.Join.dto;

import lombok.*;
import ru.kpfu.itis.androidlab.Join.form.ResponseForm;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDto implements ResponseForm {
    private Integer statusCode;
    private String token;
    private Long expiresIn;
    private Long userId;
}
