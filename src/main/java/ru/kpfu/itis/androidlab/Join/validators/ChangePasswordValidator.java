package ru.kpfu.itis.androidlab.Join.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kpfu.itis.androidlab.Join.form.ChangePasswordForm;
import ru.kpfu.itis.androidlab.Join.model.Recovery;
import ru.kpfu.itis.androidlab.Join.service.interfaces.RecoveryServiceInt;

public class ChangePasswordValidator implements Validator {

    private RecoveryServiceInt recoveryService;

    public ChangePasswordValidator(RecoveryServiceInt recoveryService) {
        this.recoveryService = recoveryService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ChangePasswordForm.class.getName().equals(aClass.getName());
    }

    @Override
    public void validate(Object o, Errors errors) {
        ChangePasswordForm changePasswordForm = (ChangePasswordForm) o;

        Recovery recovery = recoveryService.getRecovery(changePasswordForm);
        if (recovery == null) {
            errors.reject("invalid code", "Пользователь с логином ");
        }
    }
}
