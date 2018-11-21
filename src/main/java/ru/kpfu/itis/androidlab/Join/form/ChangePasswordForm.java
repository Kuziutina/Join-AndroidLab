package ru.kpfu.itis.androidlab.Join.form;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordForm {

    private String email;
    private String password;
    private String code;

}
