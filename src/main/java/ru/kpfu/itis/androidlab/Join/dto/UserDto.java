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
    private String email;
    private String username;
    private String name;
    private String lastname;
    private String phoneNumber;

    private List<SpecializationDto> specializations;

    private UserDto(User user) {
        id = user.getId();
        name = user.getName();
        lastname = user.getLastName();
        username = user.getUsername();
        email = user.getEmail();
        phoneNumber = user.getPhone();

        specializations = new ArrayList<>();

        for (Specialization specialization: user.getSpecializations()) {
            specializations.add(SpecializationDto.from(specialization));
        }
    }

    public static UserDto from(User user) {
        return new UserDto(user);
    }
}
