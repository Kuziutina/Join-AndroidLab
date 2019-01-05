package ru.kpfu.itis.androidlab.Join.controller;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.androidlab.Join.details.CustomUserDetails;
import ru.kpfu.itis.androidlab.Join.dto.ProjectDto;
import ru.kpfu.itis.androidlab.Join.dto.SimpleProjectDto;
import ru.kpfu.itis.androidlab.Join.form.InviteUserForm;
import ru.kpfu.itis.androidlab.Join.form.ProjectForm;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ProjectServiceInt;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/projects")
public class ProjectController {

    private ProjectServiceInt projectService;

    public ProjectController(ProjectServiceInt projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity getAllProjects(@RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "vacancy_name", required = false) String vacancyName,
                                         @RequestParam(value = "knowledge_level", required = false) Integer level,
                                         @RequestParam(value = "experience", required = false) Integer experience,
                                         Authentication authentication) {
        List<SimpleProjectDto> projectDtos = projectService.findProjectDtos(name, vacancyName, level, experience, (CustomUserDetails) authentication.getPrincipal());
        return ResponseEntity.ok(projectDtos);
    }

    @PostMapping
    public ResponseEntity createProject(@RequestBody ProjectForm projectForm) {
        Long userId = projectService.createProject(projectForm);

        return ResponseEntity.ok(userId);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getProject(@PathVariable Long id) {
        ProjectDto projectDto = projectService.getProjectDto(id);

        return ResponseEntity.ok(projectDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity changeProject(@PathVariable Long id, @RequestBody ProjectForm projectForm) {
        projectService.changeProject(id, projectForm);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/join")
    public ResponseEntity joinProject(@RequestBody InviteUserForm inviteUserForm) {
        projectService.joinRequest(inviteUserForm);

        return ResponseEntity.ok().build();
    }

}
