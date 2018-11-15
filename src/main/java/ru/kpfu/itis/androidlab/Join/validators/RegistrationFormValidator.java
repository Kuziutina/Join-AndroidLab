package ru.kpfu.itis.androidlab.Join.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.kpfu.itis.androidlab.Join.form.RegistrationForm;
import ru.kpfu.itis.androidlab.Join.model.Confirmation;
import ru.kpfu.itis.androidlab.Join.model.User;
import ru.kpfu.itis.androidlab.Join.repository.ConfirmationRepository;
import ru.kpfu.itis.androidlab.Join.repository.UserRepository;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;

import java.util.Optional;

@Component
public class RegistrationFormValidator implements Validator {

    private UserServiceInt userService;
    private ConfirmationRepository confirmationRepository;

    public RegistrationFormValidator(UserServiceInt userService,
                                     ConfirmationRepository confirmationRepository) {
        this.userService = userService;
        this.confirmationRepository = confirmationRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return RegistrationForm.class.getName().equals(aClass.getName());
    }

    @Override
    public void validate(Object o, Errors errors) {

        RegistrationForm registrationForm = (RegistrationForm) o;
        User user = userService.getUserByEmail(registrationForm.getEmail());
        if (user != null) {
            errors.reject("invalid login", "Пользователь с логином " + registrationForm.getEmail() + " уже существует.");
            user = null;
        }

        user = userService.getUserByUsername(registrationForm.getUsername());
        if (user != null) {
            errors.reject("invalid username", "Пользователь с именем " + registrationForm.getUsername() + " уже существует.");
            user = null;
        }

        Confirmation confirmation = confirmationRepository.findOneByEmail(registrationForm.getEmail());
        if (!confirmation.getCode().equals(registrationForm.getCode())) {
            errors.reject("invalid confirmation", "Confirmation code incorrect");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "invalid.login", "Проверьте Ваш логин.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "invalid.password", "Проверьте Ваш пароль.");

    }
}
