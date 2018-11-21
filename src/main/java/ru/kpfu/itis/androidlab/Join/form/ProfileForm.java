package ru.kpfu.itis.androidlab.Join.form;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileForm {
    private String name;
    private String lastname;
    private String username;
    private String email;
    private String phoneNumber;
}
