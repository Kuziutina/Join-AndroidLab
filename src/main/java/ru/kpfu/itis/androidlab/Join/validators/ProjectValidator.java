package ru.kpfu.itis.androidlab.Join.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kpfu.itis.androidlab.Join.form.ProjectForm;
import ru.kpfu.itis.androidlab.Join.form.SpecializationForm;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ProjectServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;

@Component
public class ProjectValidator implements Validator {

    private UserServiceInt userService;

    public ProjectValidator(UserServiceInt userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ProjectForm.class.getName().equals(aClass.getName());
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProjectForm projectForm = (ProjectForm) o;

        if (projectForm.getName() == null || projectForm.getName().isEmpty()) {
            errors.reject("invalid project name");
        }


        if (projectForm.getVacancies() != null) {
            SpecializationForm specializationForm;
            for (int i = 0; i < projectForm.getVacancies().size(); i++) {
                specializationForm = projectForm.getVacancies().get(i);
                if (specializationForm.getName() == null || specializationForm.getName().isEmpty()) {
                    errors.reject("invalid specialization name (empty)");
                    return;
                }
                //TODO O^2, need optimization
                for (int j = i+1; j < projectForm.getVacancies().size(); j++) {
                    if (specializationForm.getName().equals(projectForm.getVacancies().get(j).getName())) {
                        errors.reject("invalid specialization name (repeat)");
                    }
                }
            }
        }

    }
}
