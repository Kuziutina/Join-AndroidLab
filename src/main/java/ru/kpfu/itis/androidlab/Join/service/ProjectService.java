package ru.kpfu.itis.androidlab.Join.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.androidlab.Join.dto.ProjectDto;
import ru.kpfu.itis.androidlab.Join.dto.SimpleProjectDto;
import ru.kpfu.itis.androidlab.Join.form.InviteUserForm;
import ru.kpfu.itis.androidlab.Join.form.ProfileForm;
import ru.kpfu.itis.androidlab.Join.form.ProjectForm;
import ru.kpfu.itis.androidlab.Join.form.SpecializationForm;
import ru.kpfu.itis.androidlab.Join.model.*;
import ru.kpfu.itis.androidlab.Join.repository.ProjectRepository;
import ru.kpfu.itis.androidlab.Join.service.interfaces.NotificationServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ProjectServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.SpecializationServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService implements ProjectServiceInt {

    private UserServiceInt userService;
    private SpecializationServiceInt specializationService;
    private ProjectRepository projectRepository;
    private NotificationServiceInt notificationService;

    public ProjectService(UserServiceInt userService,
                          ProjectRepository projectRepository,
                          SpecializationServiceInt specializationService,
                          @Lazy NotificationServiceInt notificationService) {
        this.userService = userService;
        this.projectRepository = projectRepository;
        this.specializationService = specializationService;
        this.notificationService = notificationService;
    }

    private List<Project> getUserProjects(Long userId) {
        User user = userService.getUser(userId);
        return projectRepository.findAllByParticipantsContainsOrLeader(user, user);
    }

    @Override
    public List<Project> getUserOwnerProjects(User user) {
        return projectRepository.findAllByLeader(user);
    }

    @Override
    public List<ProjectDto> getUserProjectDtos(Long id) {
        List<Project> projects = getUserProjects(id);
        List<ProjectDto> projectDtos = new ArrayList<>();
        if (projects != null) {
            for (Project project: projects) {
                projectDtos.add(ProjectDto.from(project));
            }
        }

        return projectDtos;
    }

    @Override
    public Project getProject(Long id) {
        return projectRepository.getOne(id);
    }

    @Override
    public ProjectDto getProjectDto(Long id) {
        Project project = getProject(id);
        if (project == null) return null;
        return ProjectDto.from(project);
    }

    @Override
    public List<SimpleProjectDto> findProjectDtos(String name, String vacancyName, Integer knowledgeLevel, Integer experience, String principal) {
        User user = null;
        if (principal != null) {
            user = userService.getUserByEmail(principal);
        }
        //КОСТЫЫЫЫЛЬ TODO fix it

        List<Project> projects;
        List<Project> result = new ArrayList<>();
        if (name != null) {
            projects = projectRepository.searchProjectByTitle("%" + name + "%");
        }
        else {
            projects = projectRepository.findAll();
        }
        if (vacancyName != null) {
            SpecializationName specializationName = specializationService.findSpecializationName(vacancyName);
            for (Project project: projects) {
                for (Specialization specialization: project.getVacancies()) {
                    if (specialization.getSpecializationName().equals(specializationName)) {
                        if (knowledgeLevel != null) {
                            if (knowledgeLevel == specialization.getKnowledgeLevel()) {
                                if (experience != null) {
                                    if (experience == specialization.getExperience()) {
                                        result.add(project);
                                    }
                                    else break;
                                }
                                else result.add(project);
                            }
                            else break;
                        }
                        else if (experience != null) {
                            if (experience == specialization.getExperience()) {
                                result.add(project);
                            }
                        }else {
                            result.add(project);
                        }
                        break;
                    }
                }

            }
        }else {
            result = projects;
        }

        List<SimpleProjectDto> projectDtos = new ArrayList<>();
        List<Project> justThere = null;
        List<Project> justJoined = null;
        List<Project> justInvited = null;
        if (user != null) {
            justThere = getUserProjects(user.getId());
            justJoined = notificationService.getProjectUserJoined(user);
            justInvited = notificationService.getProjectUserInvited(user);
        }
        SimpleProjectDto simpleProjectDto;
        for (Project project: result) {
            simpleProjectDto = SimpleProjectDto.from(project);
            if (justThere != null && justThere.contains(project)) {
                simpleProjectDto.setStatus(1);
            }
            else if (justInvited != null && justInvited.contains(project)) {
                simpleProjectDto.setStatus(2);
            }
            else if (justJoined != null && justJoined.contains(project)) {
                simpleProjectDto.setStatus(3);
            }
            projectDtos.add(simpleProjectDto);
        }

        return projectDtos;
    }

    @Override
    public Long createProject(ProjectForm projectForm) {
        Project project = getProject(projectForm);
        return project.getId();
    }

    @Override
    public boolean changeProject(Long id, ProjectForm projectForm) {
        Project project = projectRepository.getOne(id);
        if (project == null) return false;
        //TODO Этот костыль был поставлен, чтобы убрать 2н^2 сложность.
        specializationService.deleteSpecialization(project);
        project.setTitle(projectForm.getName());
        project.setDescription(projectForm.getDescription());
        List<User> users = new ArrayList<>();
        for (ProfileForm profileForm: projectForm.getParticipants()) {
            users.add(userService.getUser(profileForm.getId()));
        }
        project.setParticipants(users);
        List<Specialization> specializations = new ArrayList<>();
        for (SpecializationForm specializationForm: projectForm.getVacancies()) {
            Specialization specialization = specializationService.addSpecialization(project, specializationForm);
            specializations.add(specialization);
        }
        project.setVacancies(specializations);
        projectRepository.save(project);

        return true;
    }

    @Override
    public void addUserToProject(User user, Project project) {
        project.getParticipants().add(user);
        projectRepository.save(project);
    }

    @Override
    public void inviteUser(InviteUserForm inviteUserForm) {
        User user = userService.getUser(inviteUserForm.getUserId());
        Project project = getProject(inviteUserForm.getProjectId());

        notificationService.addNotification(user, project, 0);
    }

    @Override
    public void joinRequest(InviteUserForm inviteUserForm) {
        User user = userService.getUser(inviteUserForm.getUserId());
        Project project = getProject(inviteUserForm.getProjectId());

        notificationService.addNotification(user, project, 2);
    }

    @Override
    public List<User> getAllParticipants(Long projectId) {
        Project project = getProject(projectId);
        List<User> users = project.getParticipants();
        users.add(project.getLeader());

        return users;
    }

    @Override
    public User getLeader(Long projectId) {
        Project project = getProject(projectId);

        return project.getLeader();
    }

    @Override
    public boolean deleteProject(Long id) {
        if (getProject(id) == null) return false;
        projectRepository.deleteById(id);
        return true;
    }

    @Override
    public void excludeUser(InviteUserForm inviteUserForm) {
        User user = userService.getUser(inviteUserForm.getUserId());
        Project project = getProject(inviteUserForm.getProjectId());

        removeUser(project, user);

        notificationService.addNotification(user, project, 10);
    }

    @Override
    public void exitFromProject(InviteUserForm inviteUserForm) {
        User user = userService.getUser(inviteUserForm.getUserId());
        Project project = getProject(inviteUserForm.getProjectId());

        removeUser(project, user);

        notificationService.addNotification(user, project, 11);
    }


    private void removeUser(Project project, User user) {
        List<User> users = project.getParticipants();
        if (user != null) {
            for (int i = 0; i < users.size(); i++) {
                if (user.getId().equals(users.get(i).getId())) {
                    users.remove(i);
                    return;
                }
            }
        }

        projectRepository.save(project);
    }


    private Project getProject(ProjectForm projectForm) {
        User user = userService.getUser(projectForm.getUserId());
        Project project = Project.builder().title(projectForm.getName())
                                    .description(projectForm.getDescription())
                                    .leader(user).build();

        projectRepository.save(project);

        Specialization specialization;
        project.setVacancies(new ArrayList<>());
        for (SpecializationForm specializationForm: projectForm.getVacancies()) {
            specialization = specializationService.addSpecialization(project, specializationForm);
            project.getVacancies().add(specialization);
        }

        return project;
    }
}
