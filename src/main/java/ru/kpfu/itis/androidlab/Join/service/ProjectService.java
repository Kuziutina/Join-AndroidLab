package ru.kpfu.itis.androidlab.Join.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.androidlab.Join.dto.ProjectDto;
import ru.kpfu.itis.androidlab.Join.dto.SimpleProjectDto;
import ru.kpfu.itis.androidlab.Join.form.InviteUserForm;
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
    public ProjectDto getProjectDto(Long id, String principal) {
        User user = userService.getUserByEmail(principal);
        Project project = getProject(id);
        if (project == null) return null;

        ProjectDto projectDto = ProjectDto.from(project);
        if (isParticipant(project, user)) projectDto.setStatus(1);
        else if (isInvited(project, user)) projectDto.setStatus(2);
        else if (isJoined(project, user)) projectDto.setStatus(3);

        return projectDto;
    }

    //TODO optimize query form db

    private boolean isParticipant(Project project, User user) {
        if (project == null || user == null) return false;

        if (project.getParticipants() != null) {
            for (User participant: project.getParticipants()) {
                if (participant.getId().equals(user.getId())) {
                    return true;
                }
            }
            return user.getId().equals(project.getLeader().getId());
        }

        return false;
    }

    private boolean isInvited(Project project, User user) {
        if (user == null || project == null) return false;
        List<Project> invited = notificationService.getProjectUserInvited(user);
        if (invited == null || invited.isEmpty()) return false;
        for (Project project1: invited) {
            if (project1.getId().equals(project.getId())) return true;
        }

        return false;
    }

    private boolean isJoined(Project project, User user) {
        if (user == null || project == null) return false;
        List<Project> joined = notificationService.getProjectUserJoined(user);
        if (joined == null || joined.isEmpty()) return false;
        for (Project project1: joined) {
            if (project1.getId().equals(project.getId())) return true;
        }

        return false;
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
            result = projectRepository.searchProjectByTitle("%" + name + "%");
        }
        else {
            result = projectRepository.findAll();
        }

        if (vacancyName != null) {
            SpecializationName specializationName = specializationService.findSpecializationName(vacancyName);
            projects = result;
            result = new ArrayList<>();
            for (Project project: projects) {
                if (project.getVacancies() != null) {
                    for (Specialization specialization : project.getVacancies()) {
                        if (specialization.getSpecializationName().equals(specializationName)) {
                            result.add(project);
                            break;
                        }
                    }
                }

            }
        }

        if (knowledgeLevel != null) {
            projects = result;
            result = new ArrayList<>();
            for (Project project: projects) {
                if (project.getVacancies() != null) {
                    for (Specialization specialization: project.getVacancies()) {
                        if (specialization.getKnowledgeLevel().equals(knowledgeLevel)) {
                            result.add(project);
                            break;
                        }
                    }
                }
            }
        }


        if (experience != null) {
            projects = result;
            result = new ArrayList<>();
            for (Project project: projects) {
                if (project.getVacancies() != null) {
                    for (Specialization specialization: project.getVacancies()) {
                        if (specialization.getExperience().equals(experience)) {
                            result.add(project);
                            break;
                        }
                    }
                }
            }
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
        //List<Specialization> specializations = new ArrayList<>();
        for (SpecializationForm specializationForm: projectForm.getVacancies()) {
            Specialization specialization = specializationService.addSpecialization(project, specializationForm);
            //specializations.add(specialization);
            project.getVacancies().add(specialization);
        }
//        project.setVacancies(specializations);
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
