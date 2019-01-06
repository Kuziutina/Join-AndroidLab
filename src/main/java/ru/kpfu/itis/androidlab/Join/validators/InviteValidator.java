package ru.kpfu.itis.androidlab.Join.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kpfu.itis.androidlab.Join.form.InviteUserForm;
import ru.kpfu.itis.androidlab.Join.form.SpecializationForm;
import ru.kpfu.itis.androidlab.Join.model.Notification;
import ru.kpfu.itis.androidlab.Join.model.Project;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.service.interfaces.NotificationServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ProjectServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;

@Component
public class InviteValidator implements Validator {

    private UserServiceInt userService;
    private ProjectServiceInt projectService;
    private NotificationServiceInt notificationService;

    public InviteValidator(UserServiceInt userService,
                           ProjectServiceInt projectService,
                           NotificationServiceInt notificationService) {
        this.userService = userService;
        this.projectService = projectService;
        this.notificationService = notificationService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return InviteUserForm.class.getName().equals(aClass.getName());
    }

    @Override
    public void validate(Object o, Errors errors) {
        InviteUserForm inviteUserForm = (InviteUserForm) o;

        User user = userService.getUser(inviteUserForm.getUserId());
        Project project = projectService.getProject(inviteUserForm.getProjectId());
        if (user == null) {
            errors.reject("user doesn't exist");
            return;
        }
        if (project == null) {
            errors.reject("project doesn't exist");
            return;
        }

        if (notificationService.checkExisting(user, project) != null) {
            errors.reject("already invited");
        }

    }
}
