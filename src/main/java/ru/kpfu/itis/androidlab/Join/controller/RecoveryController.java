package ru.kpfu.itis.androidlab.Join.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.kpfu.itis.androidlab.Join.form.ChangePasswordForm;
import ru.kpfu.itis.androidlab.Join.form.ConfirmationForm;
import ru.kpfu.itis.androidlab.Join.service.interfaces.RecoveryServiceInt;
import ru.kpfu.itis.androidlab.Join.validators.RecoveryValidator;

import javax.validation.Valid;

@Controller
public class RecoveryController extends MainController{

    private RecoveryServiceInt recoveryService;
    private RecoveryValidator recoveryValidator;

    public RecoveryController(RecoveryServiceInt recoveryService, RecoveryValidator recoveryValidator) {
        this.recoveryService = recoveryService;
        this.recoveryValidator = recoveryValidator;
    }

    @InitBinder("confirmationForm")
    public void initUserFormValidator(WebDataBinder binder) {
        binder.addValidators(recoveryValidator);
    }

    @PostMapping(value = "/recovery/email")
    public ResponseEntity emailConfirmation(@Valid @RequestBody ConfirmationForm confirmationForm,
                                            BindingResult errors) {
        if (errors.hasErrors()) {
            return createValidErrorResponse(errors);
        }

        recoveryService.sendRecoveryLetter(confirmationForm.getEmail());
        return ResponseEntity.ok().body(null);
    }

    @PostMapping(value = "/recovery")
    public ResponseEntity changePassword(@Valid @RequestBody ChangePasswordForm changePasswordForm,
                                         BindingResult errors) {
        if (errors.hasErrors()) {
            return createValidErrorResponse(errors);
        }

        recoveryService.changePassword(changePasswordForm);
        return ResponseEntity.ok().body(null);
    }
}
