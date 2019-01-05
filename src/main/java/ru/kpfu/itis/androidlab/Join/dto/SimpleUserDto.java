package ru.kpfu.itis.androidlab.Join.dto;

import lombok.*;
import ru.kpfu.itis.androidlab.Join.model.User;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleUserDto {
    private Long id;
    private String username;
    private String email;
    private String profileImage;
    private Integer status;

    //0 - no
    //1 - here
    //2 - invited

    private SimpleUserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImageLink();
        status = 0;
    }

    public static SimpleUserDto from(User user) {
        return new SimpleUserDto(user);
    }
}
