package ru.kpfu.itis.androidlab.Join.dto;

import lombok.*;
import ru.kpfu.itis.androidlab.Join.model.Specialization;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class SpecializationDto {

    private Long id;
    private Integer knowledgeLevel;
    private Integer experience;
    private String name;
    private String technologies;

    public SpecializationDto(Specialization specialization) {
        id = specialization.getId();
        name = specialization.getSpecializationName().getName();
        knowledgeLevel = specialization.getKnowledgeLevel();
        experience = specialization.getExperience();
        technologies = specialization.getTechnologies();
    }

    public static SpecializationDto from(Specialization specialization) {
        return new SpecializationDto(specialization);
    }
}
