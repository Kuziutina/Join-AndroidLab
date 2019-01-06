package ru.kpfu.itis.androidlab.Join.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kpfu.itis.androidlab.Join.form.ProfileForm;
import ru.kpfu.itis.androidlab.Join.form.SpecializationForm;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ProjectServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;

@Component
public class UserProfileValidator implements Validator {

    private UserServiceInt userService;

    public UserProfileValidator(UserServiceInt userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ProfileForm.class.getName().equals(aClass.getName());
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProfileForm profileForm = (ProfileForm) o;

        if (profileForm.getUsername() == null || profileForm.getUsername().isEmpty()) {
            errors.reject("invalid username(empty)");
            User checkUser = userService.getUserByUsername(profileForm.getUsername());
            if (checkUser != null && !checkUser.getEmail().equals(profileForm.getEmail())) {
                errors.reject("invalid username(repeat)");
            }
        }

        if (profileForm.getSpecializations() != null) {
            SpecializationForm specializationForm;
            for (int i = 0; i < profileForm.getSpecializations().size(); i++) {
                specializationForm = profileForm.getSpecializations().get(i);
                if (specializationForm.getName() == null || specializationForm.getName().isEmpty()) {
                    errors.reject("invalid specialization name (empty)");
                    return;
                }
                //TODO O^2, need optimization
                for (int j = i+1; j < profileForm.getSpecializations().size(); j++) {
                    if (specializationForm.getName().equals(profileForm.getSpecializations().get(j).getName())) {
                        errors.reject("invalid specialization name (repeat)");
                    }
                }
            }
        }
    }
}
