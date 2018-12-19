package ru.kpfu.itis.androidlab.Join.service.interfaces;

import ru.kpfu.itis.androidlab.Join.dto.ProjectDto;
import ru.kpfu.itis.androidlab.Join.dto.SimpleProjectDto;
import ru.kpfu.itis.androidlab.Join.form.ProjectForm;
import ru.kpfu.itis.androidlab.Join.model.Project;

import java.util.List;

public interface ProjectServiceInt {
    List<SimpleProjectDto> getUserProjectDtos(Long userId);
    ProjectDto getProjectDto(Long id);
    Project getProject(Long id);

    List<ProjectDto> findProjectDtos(String name, String vacancyName, Integer knowledgeLevel, Integer experience);

    Long createProject(ProjectForm projectForm);
    void changeProject(Long id, ProjectForm projectForm);
}
