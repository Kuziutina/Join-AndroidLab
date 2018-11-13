package ru.kpfu.itis.androidlab.Join.dto;

import lombok.*;
import ru.kpfu.itis.androidlab.Join.model.*;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class UserDto {
    private Long id;
    private String mail;
    private String username;
    private String name;
    private String lastName;
    private String phone;

    private List<SpecializationDto> specializations;

    private UserDto(User user) {
        id = user.getId();
        name = user.getName();
        lastName = user.getLastName();
        username = user.getUsername();
        mail = user.getEmail();
        phone = user.getPhone();

        specializations = new ArrayList<>();

        for (Specialization specialization: user.getSpecializations()) {
            specializations.add(SpecializationDto.from(specialization));
        }
    }

    public static UserDto from(User user) {
        return new UserDto(user);
    }
}
