package ru.kpfu.itis.androidlab.Join.form;


import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm {
    private String code;
    private String username;
    private String email;
    private String password;
}
