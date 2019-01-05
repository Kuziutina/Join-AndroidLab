package ru.kpfu.itis.androidlab.Join.dto;


import lombok.*;
import ru.kpfu.itis.androidlab.Join.model.Project;
import ru.kpfu.itis.androidlab.Join.model.Specialization;
import ru.kpfu.itis.androidlab.Join.model.User;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleProjectDto {

    private Long id;
    private String name;
    private String description;
    private SimpleUserDto leader;
    private List<SimpleUserDto> participants;
    private List<SpecializationDto> vacancies;

    private SimpleProjectDto(Project project) {
        this.id = project.getId();
        this.name = project.getTitle();
        this.description = project.getDescription();
        this.leader = SimpleUserDto.from(project.getLeader());

        if (project.getParticipants() != null) {
            participants = new ArrayList<>();
            for (User user: project.getParticipants()) {
                participants.add(SimpleUserDto.from(user));
            }
        }

        if (project.getVacancies() != null) {
            vacancies = new ArrayList<>();
            for (Specialization specialization: project.getVacancies()) {
                vacancies.add(SpecializationDto.from(specialization));
            }
        }
    }

    public static SimpleProjectDto from(Project project) {
        return new SimpleProjectDto(project);
    }

}
