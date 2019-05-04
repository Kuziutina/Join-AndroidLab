package ru.kpfu.itis.androidlab.Join.controller;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.androidlab.Join.details.CustomUserDetails;
import ru.kpfu.itis.androidlab.Join.dto.ProjectDto;
import ru.kpfu.itis.androidlab.Join.dto.SimpleProjectDto;
import ru.kpfu.itis.androidlab.Join.form.InviteUserForm;
import ru.kpfu.itis.androidlab.Join.form.ProjectForm;
import ru.kpfu.itis.androidlab.Join.form.ResultForm;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ProjectServiceInt;
import ru.kpfu.itis.androidlab.Join.validators.InviteValidator;
import ru.kpfu.itis.androidlab.Join.validators.ProjectValidator;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/projects")
public class ProjectController extends MainController {

    private ProjectServiceInt projectService;
    private ProjectValidator projectValidator;
    private InviteValidator inviteValidator;

    public ProjectController(ProjectServiceInt projectService,
                             ProjectValidator projectValidator,
                             InviteValidator inviteValidator) {
        this.projectService = projectService;
        this.projectValidator = projectValidator;
        this.inviteValidator = inviteValidator;
    }

    @InitBinder("projectForm")
    public void initUserFormValidator(WebDataBinder binder) {
        binder.addValidators(projectValidator);
    }


    @InitBinder("inviteUserForm")
    public void initInviteUserFormValidator(WebDataBinder binder) {
        binder.addValidators(inviteValidator);
    }

    @GetMapping
    public ResponseEntity getAllProjects(@RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "vacancy_name", required = false) String vacancyName,
                                         @RequestParam(value = "knowledge_level", required = false) Integer level,
                                         @RequestParam(value = "experience", required = false) Integer experience,
                                         Authentication authentication) {
        List<SimpleProjectDto> projectDtos = projectService.findProjectDtos(name, vacancyName, level, experience, ((User)authentication.getPrincipal()).getEmail());
        return ResponseEntity.ok(projectDtos);
    }

    @PostMapping
    public ResponseEntity createProject(@RequestBody @Valid ProjectForm projectForm,
                                        BindingResult errors) {
        if (errors.hasErrors()) {
            return createValidErrorResponse(errors);
        }

        Long userId = projectService.createProject(projectForm);

        return ResponseEntity.ok(userId);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getProject(@PathVariable Long id,
                                     Authentication authentication) {
        ProjectDto projectDto = projectService.getProjectDto(id, ((User) authentication.getPrincipal()).getEmail());
        if (projectDto == null){
            return createResponseEntity(ResultForm.builder().code(400).error("project not exists").build());
        }

        return ResponseEntity.ok(projectDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity changeProject(@PathVariable Long id,
                                        @Valid @RequestBody ProjectForm projectForm,
                                        BindingResult errors) {
        if (errors.hasErrors()) {
            return createValidErrorResponse(errors);
        }

        if (!projectService.changeProject(id, projectForm)) {
            return createResponseEntity(ResultForm.builder().code(400).error("project not exists").build());
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteProject(@PathVariable Long id) {
        if (!projectService.deleteProject(id)){
            return createResponseEntity(ResultForm.builder().code(400).error("project not exists").build());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/exclude")
    public ResponseEntity excludeUserFromProject(@Valid @RequestBody InviteUserForm inviteUserForm,
                                                 BindingResult errors) {
        if (errors.hasErrors()) {
            return createValidErrorResponse(errors);
        }

        projectService.excludeUser(inviteUserForm);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/join")
    public ResponseEntity joinProject(@Valid @RequestBody InviteUserForm inviteUserForm,
                                      BindingResult errors) {
        if (errors.hasErrors()) {
            return createValidErrorResponse(errors);
        }

        projectService.joinRequest(inviteUserForm);

        return ResponseEntity.ok().build();
    }

}
