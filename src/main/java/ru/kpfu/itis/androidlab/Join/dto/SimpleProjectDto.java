package ru.kpfu.itis.androidlab.Join.dto;


import lombok.*;
import ru.kpfu.itis.androidlab.Join.model.Project;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleProjectDto {

    private Long id;
    private String name;
    private String description;
    private String leaderName;

    private SimpleProjectDto(Project project) {
        this.id = project.getId();
        this.name = project.getTitle();
        this.description = project.getDescription();
        this.leaderName = project.getLeader().getUsername();
    }

    public static SimpleProjectDto from(Project project) {
        return new SimpleProjectDto(project);
    }
}
