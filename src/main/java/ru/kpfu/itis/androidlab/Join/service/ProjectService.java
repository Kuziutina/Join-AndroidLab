package ru.kpfu.itis.androidlab.Join.service;

import org.springframework.stereotype.Service;
import ru.kpfu.itis.androidlab.Join.dto.ProjectDto;
import ru.kpfu.itis.androidlab.Join.dto.SimpleProjectDto;
import ru.kpfu.itis.androidlab.Join.form.ProfileForm;
import ru.kpfu.itis.androidlab.Join.form.ProjectForm;
import ru.kpfu.itis.androidlab.Join.form.SpecializationForm;
import ru.kpfu.itis.androidlab.Join.model.Project;
import ru.kpfu.itis.androidlab.Join.model.Specialization;
import ru.kpfu.itis.androidlab.Join.model.SpecializationName;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.repository.ProjectRepository;
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

    public ProjectService(UserServiceInt userService, ProjectRepository projectRepository, SpecializationServiceInt specializationService) {
        this.userService = userService;
        this.projectRepository = projectRepository;
        this.specializationService = specializationService;
    }

    private List<Project> getUserProject(Long userId) {

        User user = userService.getUser(userId);
        return projectRepository.findAllByParticipantsOrLeader(user, user);
    }

    @Override
    public List<SimpleProjectDto> getUserProjectDtos(Long id) {
        List<Project> projects = getUserProject(id);
        List<SimpleProjectDto> projectDtos = new ArrayList<>();
        if (projects != null) {
            for (Project project: projects) {
                projectDtos.add(SimpleProjectDto.from(project));
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
        return ProjectDto.from(getProject(id));
    }

    @Override
    public List<ProjectDto> findProjectDtos(String name, String vacancyName, Integer knowledgeLevel, Integer experience) {

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

        List<ProjectDto> projectDtos = new ArrayList<>();

        for (Project project: result) {
            projectDtos.add(ProjectDto.from(project));
        }

        return projectDtos;
    }

    @Override
    public Long createProject(ProjectForm projectForm) {
        Project project = getProject(projectForm);
        return project.getId();
    }

    @Override
    public void changeProject(Long id, ProjectForm projectForm) {
        Project project = projectRepository.findById(id).get();
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

    }


    private Project getProject(ProjectForm projectForm) {
        User user = userService.getUser(projectForm.getUserId());
        Project project = Project.builder().title(projectForm.getName())
                                    .description(projectForm.getDescription())
                                    .leader(user).build();

        projectRepository.save(project);

        Specialization specialization;
        //TODO return null, don't need, no it's need
        project.setVacancies(new ArrayList<>());
        for (SpecializationForm specializationForm: projectForm.getVacancies()) {
            specialization = specializationService.addSpecialization(project, specializationForm);
            project.getVacancies().add(specialization);
        }

        return project;
    }
}
