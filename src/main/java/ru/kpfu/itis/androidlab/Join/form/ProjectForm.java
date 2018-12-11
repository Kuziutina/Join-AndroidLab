package ru.kpfu.itis.androidlab.Join.form;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectForm {
    private String name;
    private String description;
    private Long userId;
    private List<SpecializationForm> vacancies;
    private List<ProfileForm> participants;
}
