package ru.kpfu.itis.androidlab.Join.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kpfu.itis.androidlab.Join.form.ConfirmationForm;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.service.interfaces.RecoveryServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;

@Component
public class RecoveryValidator implements Validator {

    private UserServiceInt userService;

    public RecoveryValidator(UserServiceInt userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ConfirmationForm.class.getName().equals(aClass.getName());
    }

    @Override
    public void validate(Object o, Errors errors) {
        ConfirmationForm confirmationForm = (ConfirmationForm) o;

        User user = userService.getUserByEmail(confirmationForm.getEmail());
        if (user == null) {
            errors.reject("invalid login", "Пользователь с логином " + confirmationForm.getEmail() + " nе существует.");
        }
    }
}
