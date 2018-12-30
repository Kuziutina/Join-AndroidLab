package ru.kpfu.itis.androidlab.Join.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InviteUserForm {
    private Long userId;
    private Long projectId;
}
