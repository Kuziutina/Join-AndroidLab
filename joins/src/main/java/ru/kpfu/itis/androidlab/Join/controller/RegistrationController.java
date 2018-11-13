package ru.kpfu.itis.androidlab.Join.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.androidlab.Join.form.ConfirmationForm;
import ru.kpfu.itis.androidlab.Join.form.RegistrationForm;
import ru.kpfu.itis.androidlab.Join.service.interfaces.ConfirmationServiceInt;
import ru.kpfu.itis.androidlab.Join.service.interfaces.UserServiceInt;
import ru.kpfu.itis.androidlab.Join.validators.ConfirmationValidator;
import ru.kpfu.itis.androidlab.Join.validators.RegistrationFormValidator;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/registration")
public class RegistrationController extends MainController{
    private UserServiceInt userService;
    private ConfirmationServiceInt confirmationService;
    private RegistrationFormValidator registrationFormValidator;
    private ConfirmationValidator confirmationValidator;

    public RegistrationController(UserServiceInt userService,
                                  ConfirmationServiceInt confirmationService,
                                  ConfirmationValidator confirmationValidator,
                                  RegistrationFormValidator registrationFormValidator) {
        this.userService = userService;
        this.confirmationService = confirmationService;
        this.confirmationValidator = confirmationValidator;
        this.registrationFormValidator = registrationFormValidator;
    }

    @InitBinder("registrationForm")
    public void initUserFormValidator(WebDataBinder binder) {
        binder.addValidators(registrationFormValidator);
    }

    @InitBinder("confirmationForm")
    public void initConfirmationFormValidator(WebDataBinder binder) {
        binder.addValidators(confirmationValidator);
    }

    @PostMapping(value = "/email_confirmation")
    public ResponseEntity emailConfirmation(@Valid @RequestBody ConfirmationForm confirmationForm,
                                                                BindingResult errors) {
        if (errors.hasErrors()) {
            return createValidErrorResponse(errors);
        }

        confirmationService.sendConfirmationLetter(confirmationForm.getEmail());
        return ResponseEntity.ok().body(null);
    }

    @PostMapping
    public ResponseEntity userRegistration(@Valid @RequestBody RegistrationForm registrationForm,
                                                         BindingResult errors) {
        if (errors.hasErrors()) {
            return createValidErrorResponse(errors);
        }
        userService.registerUser(registrationForm);

        return ResponseEntity.ok(null);
    }


}
