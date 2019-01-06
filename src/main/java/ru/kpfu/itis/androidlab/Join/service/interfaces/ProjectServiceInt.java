package ru.kpfu.itis.androidlab.Join.service.interfaces;

import ru.kpfu.itis.androidlab.Join.dto.ProjectDto;
import ru.kpfu.itis.androidlab.Join.dto.SimpleProjectDto;
import ru.kpfu.itis.androidlab.Join.form.InviteUserForm;
import ru.kpfu.itis.androidlab.Join.form.ProjectForm;
import ru.kpfu.itis.androidlab.Join.model.Project;
import ru.kpfu.itis.androidlab.Join.model.User;

import java.util.List;

public interface ProjectServiceInt {

    List<Project> getUserOwnerProjects(User user);
    List<ProjectDto> getUserProjectDtos(Long userId);
    ProjectDto getProjectDto(Long id);
    Project getProject(Long id);

    //List<SimpleProjectDto> findProjectDtos(String name, String vacancyName, Integer knowledgeLevel, Integer experience, CustomUserDetails principal);

    List<SimpleProjectDto> findProjectDtos(String name, String vacancyName, Integer knowledgeLevel, Integer experience, String principal);

    Long createProject(ProjectForm projectForm);
    boolean changeProject(Long id, ProjectForm projectForm);

    void addUserToProject(User user, Project project);

    void inviteUser(InviteUserForm inviteUserForm);

    void joinRequest(InviteUserForm inviteUserForm);

    List<User> getAllParticipants(Long projectId);

    User getLeader(Long projectId);

    boolean deleteProject(Long id);
}
